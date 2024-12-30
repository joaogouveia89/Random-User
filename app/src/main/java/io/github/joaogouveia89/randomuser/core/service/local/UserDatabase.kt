package io.github.joaogouveia89.randomuser.core.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.joaogouveia89.randomuser.core.service.local.converters.InstantConverter
import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.core.service.local.entities.UserEntity

const val LOCAL_DATABASE_NAME = "random_user"

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(InstantConverter::class)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}