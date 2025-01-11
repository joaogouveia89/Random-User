package io.github.joaogouveia89.randomuser.userList.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.userList.data.repository.UserListRepositoryImpl
import io.github.joaogouveia89.randomuser.userList.data.source.UserListSourceImpl
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
import io.github.joaogouveia89.randomuser.userList.domain.source.UserListSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserListModule {
    @Provides
    @Singleton
    fun provideUserListSource(
        userDao: UserDao
    ): UserListSource = UserListSourceImpl(userDao)

    @Provides
    @Singleton
    fun provideUserListRepository(
        source: UserListSource
    ): UserListRepository = UserListRepositoryImpl(source)
}