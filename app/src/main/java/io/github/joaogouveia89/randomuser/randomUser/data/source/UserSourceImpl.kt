package io.github.joaogouveia89.randomuser.randomUser.data.source

import io.github.joaogouveia89.randomuser.core.remoteService.UserService
import io.github.joaogouveia89.randomuser.core.remoteService.model.RandomUserResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserSource
import javax.inject.Inject

class UserSourceImpl @Inject constructor(
    private val service: UserService
) : UserSource {
    override suspend fun getRandomUser(): RandomUserResponse {
        val user = service.getRandomUser()
        return user
    }
}