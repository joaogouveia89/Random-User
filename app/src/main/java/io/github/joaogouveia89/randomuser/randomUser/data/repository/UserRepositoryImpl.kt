package io.github.joaogouveia89.randomuser.randomUser.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.palette.graphics.Palette
import io.github.joaogouveia89.randomuser.core.di.IoDispatcher
import io.github.joaogouveia89.randomuser.core.service.remote.model.mappers.asUser
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserRepositoryFetchResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.repository.UserSaveState
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserLocalSource
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSource
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSourceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UserRepositoryImpl @Inject constructor(
    private val remoteSource: UserRemoteSource,
    private val localSource: UserLocalSource,
    @IoDispatcher private val dispatcher: CoroutineContext
) : UserRepository {
    private val fallbackColors = Pair("#000000", "#FFFFFF")

    override fun getRandomUser(): Flow<UserRepositoryFetchResponse> = flow {
        emit(UserRepositoryFetchResponse.Loading)

        val remoteResponse = remoteSource
            .getRandomUser()

        when (remoteResponse) {
            is UserRemoteSourceResponse.Success -> {
                val remoteResponseUser = remoteResponse
                    .response
                    .results
                    .first()
                // Get the flag URL based on the user's nationality
                val flagUrl = "https://flagsapi.com/${remoteResponseUser.nat}/flat/64.png"

                // Analyze the image and get the colors
                val colors = try {
                    Pair(analyzeImageFromUrl(flagUrl), false)
                } catch (e: IOException) {
                    e.message?.let { Log.e(TAG, it) }
                    Pair(fallbackColors, true)
                }

                emit(
                    UserRepositoryFetchResponse.Success(
                        user = remoteResponseUser.asUser(colors.first),
                        isColorAnalysisError = colors.second
                    )
                )
            }

            is UserRemoteSourceResponse.Error -> {
                Log.e(TAG, remoteResponse.message)
                emit(UserRepositoryFetchResponse.SourceError)
            }
        }


    }.flowOn(dispatcher)

    override fun saveUser(user: User): Flow<UserSaveState> = flow {
        emit(UserSaveState.Loading)
        val id = localSource.saveUser(user)
        if(id == null){
            emit(UserSaveState.Error)
        }else{
            emit(UserSaveState.Success(id))
        }
    }.flowOn(dispatcher)

    // Suspend function to download the image and extract the colors
    private suspend fun analyzeImageFromUrl(url: String): Pair<String, String> =
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            val inputStream =
                response.body?.byteStream() ?: throw IOException("Empty response body")
            val bitmap = BitmapFactory.decodeStream(inputStream)
                ?: throw IOException("Failed to decode bitmap")

            return@withContext analyzeColors(bitmap)
        }

    // Extract dominant and vibrant colors from the bitmap
    private fun analyzeColors(bitmap: Bitmap): Pair<String, String> {
        val palette = Palette.from(bitmap).generate()

        val colors = listOf(
            palette.getDominantColor(Color.BLACK),
            palette.getVibrantColor(Color.WHITE),
            palette.getMutedColor(Color.WHITE),
            palette.getLightVibrantColor(Color.WHITE),
            palette.getLightMutedColor(Color.WHITE),
            palette.getDarkVibrantColor(Color.WHITE),
            palette.getDarkMutedColor(Color.WHITE)
        ).distinct()

        return Pair(
            String.format("#%06X", (0xFFFFFF and colors.first())),
            String.format("#%06X", (0xFFFFFF and colors[1]))
        )
    }

    companion object {
        val TAG = UserRepositoryImpl::class.java.name
    }
}