package io.github.joaogouveia89.randomuser.userDetail.domain.repository

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.coroutines.flow.Flow

sealed class UserDetailGetState {
    data object Loading : UserDetailGetState()
    data class Success(val user: User) : UserDetailGetState()
    data class Error(val errorMessage: String) : UserDetailGetState()
}

interface UserDetailRepository {
    suspend fun getUser(userId: Long): Flow<UserDetailGetState>
}