package io.github.joaogouveia89.randomuser.randomUser.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.joaogouveia89.randomuser.core.remoteService.UserService
import io.github.joaogouveia89.randomuser.randomUser.data.repository.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.randomUser.data.source.UserSourceImpl
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RandomUserModule {

    @Provides
    @Singleton
    fun provideRandomUserSource(
        service: UserService
    ): UserSource = UserSourceImpl(service)

    @Provides
    @Singleton
    fun provideRandomUserRepository(
        source: UserSource
    ): UserRepository = UserRepositoryImpl(source)
}