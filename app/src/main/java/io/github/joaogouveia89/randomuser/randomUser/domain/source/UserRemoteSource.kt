package io.github.joaogouveia89.randomuser.randomUser.domain.source

import io.github.joaogouveia89.randomuser.randomUser.data.source.UserRemoteSourceResponse

interface UserRemoteSource {
    suspend fun getRandomUser(): UserRemoteSourceResponse
}