package io.github.joaogouveia89.randomuser.randomUser.domain.source

import io.github.joaogouveia89.randomuser.core.service.remote.model.RandomUserResponse

interface UserRemoteSource {
    suspend fun getRandomUser(): RandomUserResponse
}