package io.github.joaogouveia89.randomuser.randomUser.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.core.service.remote.UserService
import io.github.joaogouveia89.randomuser.randomUser.data.repository.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.randomUser.data.source.UserLocalSourceImpl
import io.github.joaogouveia89.randomuser.randomUser.data.source.UserRemoteSourceImpl
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserLocalSource
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSource
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RandomUserModule {

    @Provides
    @ViewModelScoped
    fun provideRandomUserRemoteSource(
        service: UserService
    ): UserRemoteSource = UserRemoteSourceImpl(service)

    @Provides
    @ViewModelScoped
    fun provideRandomUserLocalSource(
        userDao: UserDao
    ): UserLocalSource = UserLocalSourceImpl(userDao)

    @Provides
    @ViewModelScoped
    fun provideRandomUserRepository(
        remoteSource: UserRemoteSource,
        localSource: UserLocalSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): UserRepository = UserRepositoryImpl(
        remoteSource = remoteSource,
        localSource = localSource,
        dispatcher = dispatcher
    )
}