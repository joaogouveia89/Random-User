package io.github.joaogouveia89.randomuser.randomUser.data.source

import io.github.joaogouveia89.randomuser.core.service.remote.UserService
import io.github.joaogouveia89.randomuser.core.service.remote.model.RandomUserResponse
import io.github.joaogouveia89.randomuser.randomUser.domain.source.UserRemoteSource
import retrofit2.HttpException
import javax.inject.Inject

class UserRemoteSourceImpl @Inject constructor(
    private val service: UserService
) : UserRemoteSource {
    override suspend fun getRandomUser(): UserRemoteSourceResponse {
        try {
            val user = service.getRandomUser()
            return UserRemoteSourceResponse.Success(user)
        }catch (exception: HttpException){
            return UserRemoteSourceResponse.Error(
                "${exception.code()} - ${exception.message()}"
            )
        }
    }
}