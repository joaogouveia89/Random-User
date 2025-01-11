package io.github.joaogouveia89.randomuser.userList.data.source

import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.core.service.local.entities.asUsers
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.userList.domain.source.UserListSource
import javax.inject.Inject

class UserListSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserListSource {
    override suspend fun getUsers(): List<User> =
        userDao
            .getUsersEntities()
            .asUsers()
}