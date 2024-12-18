package io.github.joaogouveia89.randomuser.remoteService

import com.google.gson.GsonBuilder
import io.github.joaogouveia89.randomuser.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS = 15L
private const val BASE_URL = "https://randomuser.me/"

class RandomUserRetrofit {

    private val loggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private val paramsInterceptor = ParamsInterceptor()

    private val httpClient: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(paramsInterceptor)
            .addInterceptor(loggingInterceptor)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    private val gsonConverterFactory: GsonConverterFactory =
        GsonConverterFactory
            .create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )

    val service: UserService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(UserService::class.java)
}