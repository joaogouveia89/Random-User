package io.github.joaogouveia89.randomuser.randomUser.data.source

import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.core.service.local.entities.asEntity
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserLocalSource
import javax.inject.Inject

class UserLocalSourceImpl @Inject constructor(
    private val userDao: UserDao
) : UserLocalSource {
    override suspend fun saveUser(user: User): Long? {
        try {
            return userDao.insert(user.asEntity())
        } catch (e: Exception) {
            return null
        }
    }
}