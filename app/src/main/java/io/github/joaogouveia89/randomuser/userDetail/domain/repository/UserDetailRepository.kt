package io.github.joaogouveia89.randomuser.userDetail.domain.repository

import io.github.joaogouveia89.randomuser.core.model.User
import kotlinx.coroutines.flow.Flow

sealed class UserDetailGetState {
    data object Loading : UserDetailGetState()
    data class Success(val user: User) : UserDetailGetState()
    data class Error(val errorMessage: String) : UserDetailGetState()
}

sealed class UserDetailDeleteState {
    data object Loading : UserDetailDeleteState()
    data object Success : UserDetailDeleteState()
    data object Error : UserDetailDeleteState()
}

interface UserDetailRepository {
    suspend fun getUser(userId: Long): Flow<UserDetailGetState>
    suspend fun deleteUser(id: Long): Flow<UserDetailDeleteState>
}