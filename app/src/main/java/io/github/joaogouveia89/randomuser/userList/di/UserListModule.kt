package io.github.joaogouveia89.randomuser.userList.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.userList.data.repository.UserListRepositoryImpl
import io.github.joaogouveia89.randomuser.userList.data.source.UserListSourceImpl
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
import io.github.joaogouveia89.randomuser.userList.domain.source.UserListSource

@Module
@InstallIn(ViewModelComponent::class)
object UserListModule {
    @Provides
    @ViewModelScoped
    fun provideUserListSource(
        userDao: UserDao
    ): UserListSource = UserListSourceImpl(userDao)

    @Provides
    @ViewModelScoped
    fun provideUserListRepository(
        source: UserListSource
    ): UserListRepository = UserListRepositoryImpl(source)
}