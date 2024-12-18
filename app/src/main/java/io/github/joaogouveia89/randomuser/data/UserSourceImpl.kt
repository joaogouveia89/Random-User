package io.github.joaogouveia89.randomuser.data

import android.util.Log
import io.github.joaogouveia89.randomuser.domain.UserSource
import io.github.joaogouveia89.randomuser.remoteService.UserService
import io.github.joaogouveia89.randomuser.remoteService.model.RandomUserResponse

class UserSourceImpl(
    private val service: UserService
): UserSource {
    override suspend fun getRandomUser(): RandomUserResponse {
        val user = service.getRandomUser()
        Log.i("JOAODEBUG", user.toString())
        return user
    }
}