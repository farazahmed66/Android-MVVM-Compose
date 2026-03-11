package com.faraz.codewars.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.faraz.codewars.entity.RemoteKeys
import com.faraz.codewars.models.UserChallengeData

@Database(
    entities = [RemoteKeys::class, UserChallengeData::class],
    version = 1003,
    exportSchema = true
)
@TypeConverters(TypeResponseConvertor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun starWarsDao(): StarWarsDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}