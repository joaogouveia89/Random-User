package io.github.joaogouveia89.randomuser.core.remoteService

import io.github.joaogouveia89.randomuser.core.remoteService.model.RandomUserResponse
import retrofit2.http.GET

interface UserService {
    @GET("/api")
    suspend fun getRandomUser(
    ): RandomUserResponse
}