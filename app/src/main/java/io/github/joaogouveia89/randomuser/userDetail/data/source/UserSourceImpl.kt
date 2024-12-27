package io.github.joaogouveia89.randomuser.userDetail.data.source

import io.github.joaogouveia89.randomuser.core.remoteService.UserService
import io.github.joaogouveia89.randomuser.core.remoteService.model.RandomUserResponse
import io.github.joaogouveia89.randomuser.userDetail.domain.source.UserSource

class UserSourceImpl(
    private val service: UserService
) : UserSource {
    override suspend fun getRandomUser(): RandomUserResponse {
        val user = service.getRandomUser()
        return user
    }
}