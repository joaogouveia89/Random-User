package io.github.joaogouveia89.randomuser.core.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.joaogouveia89.randomuser.BuildConfig
import io.github.joaogouveia89.randomuser.core.service.remote.LogJsonInterceptor
import io.github.joaogouveia89.randomuser.core.service.remote.ParamsInterceptor
import io.github.joaogouveia89.randomuser.core.service.remote.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT_SECONDS = 15L
    private const val BASE_URL = "https://randomuser.me/"

    @Provides
    fun provideParamsInterceptor(): ParamsInterceptor = ParamsInterceptor()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    fun provideLogJsonInterceptor(): LogJsonInterceptor =
        LogJsonInterceptor()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory
            .create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )

    @Provides
    fun provideOkHttpClient(
        paramsInterceptor: ParamsInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        logJsonInterceptor: LogJsonInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(paramsInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(logJsonInterceptor)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideRandomUserService(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): UserService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(UserService::class.java)
}