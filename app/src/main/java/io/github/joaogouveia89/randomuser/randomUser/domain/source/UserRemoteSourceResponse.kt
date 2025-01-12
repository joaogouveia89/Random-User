package io.github.joaogouveia89.randomuser.randomUser.domain.source

import io.github.joaogouveia89.randomuser.core.service.remote.model.RandomUserResponse

sealed class UserRemoteSourceResponse {
    data class Success(val response: RandomUserResponse) : UserRemoteSourceResponse()
    data class Error(val message: String) : UserRemoteSourceResponse()
}