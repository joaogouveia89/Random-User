package io.github.joaogouveia89.randomuser.core.remoteService

import retrofit2.http.GET

interface UserService {
    @GET("/api")
    suspend fun getRandomUser(
    ): io.github.joaogouveia89.randomuser.core.remoteService.model.RandomUserResponse
}