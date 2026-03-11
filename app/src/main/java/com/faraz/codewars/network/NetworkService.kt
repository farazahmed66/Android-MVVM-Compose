package com.faraz.codewars.network

import com.faraz.codewars.models.AuthoredChallenge
import com.faraz.codewars.models.ChallengeDetails
import com.faraz.codewars.models.User
import com.faraz.codewars.models.UserChallenge
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("users/{query}")
    suspend fun searchUser(@Path("query") query: String): User

    @GET("users/{user}/code-challenges/completed")
    suspend fun getCompletedChallenge(
        @Path("user") user: String,
        @Query("page") page: Int
    ): UserChallenge

    @GET("users/{user}/code-challenges/authored")
    suspend fun getAuthoredChallenge(
        @Path("user") user: String
    ): AuthoredChallenge

    @GET("code-challenges/{challengeId}")
    suspend fun getChallengeDetails(
        @Path("challengeId") challengeId: String
    ): ChallengeDetails

}