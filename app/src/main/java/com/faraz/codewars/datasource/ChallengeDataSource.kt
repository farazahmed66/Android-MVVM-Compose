package com.faraz.codewars.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faraz.codewars.models.UserChallengeData
import com.faraz.codewars.network.NetworkService
import com.faraz.codewars.remotemediator.UserRemoteMediator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

class ChallengeDataSource @AssistedInject constructor (
    private val networkService: NetworkService,
    @Assisted private val userName: String) : PagingSource<Int, UserChallengeData>() {

    @AssistedFactory
    interface Factory {
        fun create(userName: String): ChallengeDataSource
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserChallengeData> {
        val page = params.key ?: 0
        return try {
            val response = networkService.getCompletedChallenge(userName, page)
            val data = response.data ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            val error = IOException("Please Check Internet Connection")
            LoadResult.Error(error)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, UserChallengeData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}