package io.github.joaogouveia89.randomuser.remoteService

import io.github.joaogouveia89.randomuser.remoteService.model.RandomUserResponse
import retrofit2.http.GET

interface UserService {
    @GET("/api")
    suspend fun getRandomUser(
    ): RandomUserResponse
}