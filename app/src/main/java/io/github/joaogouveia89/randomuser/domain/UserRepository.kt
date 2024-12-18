package io.github.joaogouveia89.randomuser.domain

import io.github.joaogouveia89.randomuser.data.model.User
import kotlinx.coroutines.flow.Flow

sealed class UserFetchState {
    data object Loading : UserFetchState()
    data class Success(val user: User) : UserFetchState()
    data class Error(val errorMessage: String) : UserFetchState()
}

interface UserRepository {
    fun getRandomUser(): Flow<UserFetchState>
}