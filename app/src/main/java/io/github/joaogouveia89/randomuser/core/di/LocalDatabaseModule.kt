package io.github.joaogouveia89.randomuser.core.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.joaogouveia89.randomuser.core.service.local.LOCAL_DATABASE_NAME
import io.github.joaogouveia89.randomuser.core.service.local.UserDatabase
import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseModule {
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext context: Context,
    ): UserDatabase = Room.databaseBuilder(
        context,
        UserDatabase::class.java, LOCAL_DATABASE_NAME
    ).build()

    @Provides
    fun provideUserDao(
        database: UserDatabase,
    ): UserDao = database.userDao()
}