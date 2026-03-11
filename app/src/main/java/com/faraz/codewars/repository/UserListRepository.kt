package com.faraz.codewars.repository

import com.faraz.codewars.base.BaseRepository
import com.faraz.codewars.models.User
import com.faraz.codewars.network.NetworkService
import com.faraz.codewars.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserListRepository @Inject constructor(
    private val networkService: NetworkService,
) : BaseRepository() {

    fun searchUser(query: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        emit(safeApiCall {
            networkService.searchUser(query)
        })
    }.flowOn(Dispatchers.IO)

}