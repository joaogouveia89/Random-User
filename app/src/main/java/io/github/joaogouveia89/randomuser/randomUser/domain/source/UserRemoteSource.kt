package io.github.joaogouveia89.randomuser.randomUser.domain.source

interface UserRemoteSource {
    suspend fun getRandomUser(): UserRemoteSourceResponse
}