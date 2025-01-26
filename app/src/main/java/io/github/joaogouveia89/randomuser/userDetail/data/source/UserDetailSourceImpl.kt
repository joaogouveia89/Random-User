package io.github.joaogouveia89.randomuser.userDetail.data.source

import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.core.service.local.entities.asUser
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.userDetail.domain.source.UserDetailSource
import javax.inject.Inject

class UserDetailSourceImpl @Inject constructor(
    val userDao: UserDao
): UserDetailSource {
    override suspend fun getUser(userId: Long): User =
        userDao.getUser(userId).asUser()

    override suspend fun deleteUser(userId: Long) =
        userDao.delete(userId)
}