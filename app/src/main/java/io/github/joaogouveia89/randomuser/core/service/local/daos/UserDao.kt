package io.github.joaogouveia89.randomuser.core.service.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.joaogouveia89.randomuser.core.service.local.entities.UserEntity

@Dao
interface UserDao {
    @Insert
    fun insert(user: UserEntity): Long

    @Query("SELECT * FROM userEntity")
    fun getUsersEntities(): List<UserEntity>

    @Query("SELECT * FROM userentity WHERE id=:id")
    fun getUser(id: Long): UserEntity

    @Query("DELETE FROM userentity WHERE id=:id")
    fun delete(id: Long)

    @Query("DELETE FROM userentity WHERE id in (:ids)")
    fun deleteMultiple(ids: List<Long>)
}