package io.github.joaogouveia89.randomuser.randomUser.domain.repository

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.coroutines.flow.Flow

sealed class UserSaveState {
    data object Loading : UserSaveState()
    data class Success(val id: Long) : UserSaveState()
    data class Error(val errorMessage: String) : UserSaveState()
}

interface UserRepository {
    fun getRandomUser(): Flow<UserRepositoryFetchResponse>
    fun saveUser(user: User): Flow<UserSaveState>
}