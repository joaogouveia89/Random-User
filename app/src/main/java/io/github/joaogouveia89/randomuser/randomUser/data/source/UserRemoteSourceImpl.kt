package io.github.joaogouveia89.randomuser.randomUser.data.source

import io.github.joaogouveia89.randomuser.core.service.remote.UserService
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSource
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSourceResponse
import javax.inject.Inject

class UserRemoteSourceImpl @Inject constructor(
    private val service: UserService
) : UserRemoteSource {
    override suspend fun getRandomUser(): UserRemoteSourceResponse {
        return try {
            val user = service.getRandomUser()
            UserRemoteSourceResponse.Success(user)
        } catch (exception: Exception) {
            UserRemoteSourceResponse.Error(exception.message ?: "UserRemoteSource Generic Error")
        }
    }
}