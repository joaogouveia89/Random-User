package io.github.joaogouveia89.randomuser.core.service.local.daos

import androidx.room.Dao
import androidx.room.Insert
import io.github.joaogouveia89.randomuser.core.service.local.entities.UserEntity

@Dao
interface UserDao {
    @Insert
    fun insert(user: UserEntity): Long
}