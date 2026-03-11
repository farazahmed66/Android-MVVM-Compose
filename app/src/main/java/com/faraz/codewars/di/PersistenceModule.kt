package com.faraz.codewars.di

import android.content.Context
import androidx.room.Room
import com.faraz.codewars.persistence.AppDatabase
import com.faraz.codewars.persistence.RemoteKeysDao
import com.faraz.codewars.persistence.StarWarsDao
import com.faraz.codewars.persistence.TypeResponseConvertor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        typeResponseConvertor: TypeResponseConvertor
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "StarWars.db"
    ).addTypeConverter(typeResponseConvertor)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()

    @Provides
    fun provideStarWarsDao(appDatabase: AppDatabase): StarWarsDao = appDatabase.starWarsDao()

    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao =
        appDatabase.remoteKeysDao()
}