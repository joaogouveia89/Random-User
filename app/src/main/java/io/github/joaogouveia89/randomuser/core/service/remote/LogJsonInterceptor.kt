package io.github.joaogouveia89.randomuser.core.service.remote

import android.util.Log
import io.github.joaogouveia89.randomuser.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException


class LogJsonInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val response: Response = chain.proceed(request)
        val rawJson: String = response.body?.string() ?: ""

        Log.d(BuildConfig.APPLICATION_ID, String.format("raw JSON response is: %s", rawJson))

        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
            .body(rawJson.toResponseBody(response.body?.contentType())).build()
    }
}