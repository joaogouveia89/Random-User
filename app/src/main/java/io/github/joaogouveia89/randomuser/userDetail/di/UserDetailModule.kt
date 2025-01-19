package io.github.joaogouveia89.randomuser.userDetail.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.service.local.daos.UserDao
import io.github.joaogouveia89.randomuser.userDetail.data.repository.UserDetailRepositoryImpl
import io.github.joaogouveia89.randomuser.userDetail.data.source.UserDetailSourceImpl
import io.github.joaogouveia89.randomuser.userDetail.domain.repository.UserDetailRepository
import io.github.joaogouveia89.randomuser.userDetail.domain.source.UserDetailSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDetailModule {

    @Provides
    @Singleton
    fun provideUserDetailSource(
        userDao: UserDao
    ): UserDetailSource = UserDetailSourceImpl(userDao = userDao)

    @Provides
    @Singleton
    fun provideUserDetailRepository(
        userDetailSource: UserDetailSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): UserDetailRepository = UserDetailRepositoryImpl(
        userDetailSource = userDetailSource,
        dispatcher = dispatcher
    )
}