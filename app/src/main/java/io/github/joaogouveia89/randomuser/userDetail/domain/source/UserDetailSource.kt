package io.github.joaogouveia89.randomuser.userDetail.domain.source

import io.github.joaogouveia89.randomuser.core.model.User

interface UserDetailSource {
    suspend fun getUser(userId: Long): User
    suspend fun deleteUser(userId: Long)
}