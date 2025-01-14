package io.github.joaogouveia89.randomuser.randomUser.domain.repository

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User

sealed class UserRepositoryFetchResponse {
    data object Loading : UserRepositoryFetchResponse()
    data object SourceError : UserRepositoryFetchResponse()
    data class Success(val user: User, val isColorAnalysisError: Boolean = false) :
        UserRepositoryFetchResponse()
}