package com.faraz.codewars.repository

import com.faraz.codewars.base.BaseRepository
import com.faraz.codewars.models.AuthoredChallenge
import com.faraz.codewars.network.NetworkService
import com.faraz.codewars.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthoredChallengeRepository @Inject constructor(
    private val networkService: NetworkService
) : BaseRepository() {

    fun getAuthoredChallenge(user: String): Flow<Resource<AuthoredChallenge>> = flow {
        emit(Resource.Loading)
        emit(safeApiCall {
            networkService.getAuthoredChallenge(user)
        })
    }.flowOn(Dispatchers.IO)

}