package com.faraz.codewars.repository

import com.faraz.codewars.base.BaseRepository
import com.faraz.codewars.models.ChallengeDetails
import com.faraz.codewars.network.NetworkService
import com.faraz.codewars.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChallengeDetailRepository  @Inject constructor(
    private val networkService: NetworkService
) : BaseRepository() {

    fun getChallengeDetails(challengeId: String): Flow<Resource<ChallengeDetails>> = flow {
        emit(Resource.Loading)
        emit(
            safeApiCall {
                networkService.getChallengeDetails(challengeId)
            }
        )
    }.flowOn(Dispatchers.IO)
}