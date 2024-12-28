package io.github.joaogouveia89.randomuser.randomUser.domain.source

import io.github.joaogouveia89.randomuser.core.remoteService.model.RandomUserResponse

interface UserSource {
    suspend fun getRandomUser(): RandomUserResponse
}