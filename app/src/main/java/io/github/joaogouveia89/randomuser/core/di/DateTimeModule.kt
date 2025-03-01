package io.github.joaogouveia89.randomuser.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock

@Module
@InstallIn(SingletonComponent::class)
object DateTimeModule {
    @Provides
    fun provideClock(): Clock = Clock.System
}