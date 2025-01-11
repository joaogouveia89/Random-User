package io.github.joaogouveia89.randomuser.userList.domain.source

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User

interface UserListSource {
    suspend fun getUsers(): List<User>
}