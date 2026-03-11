package com.faraz.codewars.repository

import androidx.room.withTransaction
import com.faraz.codewars.base.BaseRepository
import com.faraz.codewars.network.NetworkService
import com.faraz.codewars.persistence.AppDatabase
import javax.inject.Inject

class UserDetailsRepository @Inject constructor(
    private val db: AppDatabase
) : BaseRepository() {

    suspend fun deleteUserData(){
        db.withTransaction {
            db.starWarsDao().clearRepos()
            db.remoteKeysDao().clearRemoteKeys()
        }
    }

}