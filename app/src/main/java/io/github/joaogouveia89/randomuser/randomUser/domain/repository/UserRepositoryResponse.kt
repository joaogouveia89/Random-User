package io.github.joaogouveia89.randomuser.randomUser.domain.repository

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User

sealed class UserRepositoryResponse {
    data object Loading : UserRepositoryResponse()
    data class SourceError(val message: String) : UserRepositoryResponse()
    data class Success(val user: User, val isColorAnalysisError: Boolean = false) :
        UserRepositoryResponse()
}