package com.faraz.codewars.remotemediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.faraz.codewars.entity.RemoteKeys
import com.faraz.codewars.models.UserChallengeData
import com.faraz.codewars.network.NetworkService
import com.faraz.codewars.persistence.AppDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator @AssistedInject constructor (
    private val service: NetworkService,
    private val db: AppDatabase,
    @Assisted private val userName: String
) : RemoteMediator<Int, UserChallengeData>() {

    @AssistedFactory
    interface Factory {
        fun create(userName: String): UserRemoteMediator
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserChallengeData>
    ): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> 0

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
//                val remoteKey = db.remoteKeysDao().getKeys().firstOrNull()
                val remoteKey = db.remoteKeysDao().getRemoteKey()
                if (remoteKey == null || remoteKey.isEndReached) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                remoteKey.nextKey ?: return MediatorResult.Success(true)
            }
        }

        return try {
            Log.d("Mediator", "NETWORK CALL page=$page")
            val apiResponse = service.getCompletedChallenge(userName, page)
            val challengeList = apiResponse.data.orEmpty()

            val endOfPaginationReached =
                challengeList.isEmpty() || page >= apiResponse.totalPages - 1

            db.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.starWarsDao().clearRepos()
                }

                val nextKey = page + 1

                db.remoteKeysDao().insertKey(
                    RemoteKeys(
                        id = 0,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )

                db.starWarsDao().insertMultipleUsers(challengeList)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}