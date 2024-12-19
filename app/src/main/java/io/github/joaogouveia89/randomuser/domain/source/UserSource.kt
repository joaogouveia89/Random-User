package io.github.joaogouveia89.randomuser.domain.source

import io.github.joaogouveia89.randomuser.remoteService.model.RandomUserResponse

interface UserSource {
    suspend fun getRandomUser(): RandomUserResponse
}