package io.github.joaogouveia89.randomuser.randomUser.domain.source

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User

interface UserLocalSource {
    suspend fun saveUser(user: User): Long?
}