package io.github.joaogouveia89.randomuser.randomUser.data.source

import io.github.joaogouveia89.randomuser.core.service.remote.UserService
import io.github.joaogouveia89.randomuser.core.service.remote.model.RandomUserResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSource
import javax.inject.Inject

class UserRemoteSourceImpl @Inject constructor(
    private val service: UserService
) : UserRemoteSource {
    override suspend fun getRandomUser(): RandomUserResponse {
        val user = service.getRandomUser()
        return user
    }
}