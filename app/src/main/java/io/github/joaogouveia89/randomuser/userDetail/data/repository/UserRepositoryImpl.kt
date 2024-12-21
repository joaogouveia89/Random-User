package io.github.joaogouveia89.randomuser.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.palette.graphics.Palette
import io.github.joaogouveia89.randomuser.domain.repository.UserFetchState
import io.github.joaogouveia89.randomuser.domain.repository.UserRepository
import io.github.joaogouveia89.randomuser.domain.source.UserSource
import io.github.joaogouveia89.randomuser.core.remoteService.model.mappers.asUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class UserRepositoryImpl(
    private val source: UserSource
) : UserRepository {
    override fun getRandomUser(): Flow<UserFetchState> = flow {
        emit(UserFetchState.Loading)

        val user = source
            .getRandomUser()
            .results
            .first()

        // Get the flag URL based on the user's nationality
        val flagUrl = "https://flagsapi.com/${user.nat}/flat/64.png"

        // Analyze the image and get the colors
        val colors = try {
            analyzeImageFromUrl(flagUrl)
        } catch (e: IOException) {
            Pair("#000000", "#FFFFFF") // Default fallback colors
        }

        emit(UserFetchState.Success(user.asUser(colors)))
    }.flowOn(Dispatchers.IO)

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
}