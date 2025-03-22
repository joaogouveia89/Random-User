package io.github.joaogouveia89.randomuser.userList.domain.repository

import io.github.joaogouveia89.randomuser.core.model.User
import kotlinx.coroutines.flow.Flow


sealed class UserListGetState {
    data object Loading : UserListGetState()
    data class Success(val users: List<User>) : UserListGetState()
    data object Error : UserListGetState()
}

sealed class UsersDeleteState {
    data object Loading : UsersDeleteState()
    data object Success : UsersDeleteState()
    data object Error : UsersDeleteState()
}

interface UserListRepository {
    suspend fun getUsers(): Flow<UserListGetState>
    suspend fun deleteUsers(users: List<User>): Flow<UsersDeleteState>
}