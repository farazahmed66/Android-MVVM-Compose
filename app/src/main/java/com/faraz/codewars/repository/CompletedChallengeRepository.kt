package com.faraz.codewars.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.faraz.codewars.base.BaseRepository
import com.faraz.codewars.datasource.ChallengeDataSource
import com.faraz.codewars.models.UserChallengeData
import com.faraz.codewars.network.NetworkService
import com.faraz.codewars.persistence.AppDatabase
import com.faraz.codewars.remotemediator.UserRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CompletedChallengeRepository @Inject constructor(
    private val remoteMediatorFactory: UserRemoteMediator.Factory,
    private val challengeDataSourceFactory: ChallengeDataSource.Factory
) : BaseRepository() {

    @ExperimentalPagingApi
    fun getCompletedChallenge(userName: String): Flow<PagingData<UserChallengeData>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediatorFactory.create(userName),
            pagingSourceFactory = { challengeDataSourceFactory.create(userName) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}