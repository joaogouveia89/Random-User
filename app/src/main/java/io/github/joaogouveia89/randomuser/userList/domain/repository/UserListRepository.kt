package io.github.joaogouveia89.randomuser.userList.domain.repository

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.coroutines.flow.Flow


sealed class UserListGetState {
    data object Loading : UserListGetState()
    data class Success(val users: List<User>) : UserListGetState()
    data object Error : UserListGetState()
}

interface UserListRepository {
    suspend fun getUsers(): Flow<UserListGetState>
}