package io.github.joaogouveia89.randomuser.userList.domain.source

import io.github.joaogouveia89.randomuser.core.model.User

interface UserListSource {
    suspend fun getUsers(): List<User>
    suspend fun deleteUsers(users: List<User>)
}