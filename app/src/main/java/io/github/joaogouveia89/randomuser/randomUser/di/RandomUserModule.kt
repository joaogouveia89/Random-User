package io.github.joaogouveia89.randomuser.randomUser.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.core.service.remote.UserService
import io.github.joaogouveia89.randomuser.randomUser.data.repository.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.randomUser.data.source.UserLocalSourceImpl
import io.github.joaogouveia89.randomuser.randomUser.data.source.UserRemoteSourceImpl
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserLocalSource
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RandomUserModule {

    @Provides
    @Singleton
    fun provideRandomUserRemoteSource(
        service: UserService
    ): UserRemoteSource = UserRemoteSourceImpl(service)

    @Provides
    @Singleton
    fun provideRandomUserLocalSource(
        userDao: UserDao
    ): UserLocalSource = UserLocalSourceImpl(userDao)

    @Provides
    @Singleton
    fun provideRandomUserRepository(
        remoteSource: UserRemoteSource,
        localSource: UserLocalSource
    ): UserRepository = UserRepositoryImpl(
        remoteSource = remoteSource,
        localSource = localSource
    )
}