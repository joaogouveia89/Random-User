package io.github.joaogouveia89.randomuser.core.service.remote

import io.github.joaogouveia89.randomuser.core.service.remote.model.RandomUserResponse
import retrofit2.http.GET

interface UserService {
    @GET("/api")
    suspend fun getRandomUser(
    ): RandomUserResponse
}