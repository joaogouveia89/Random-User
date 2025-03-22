package io.github.joaogouveia89.randomuser.userList.data.repository

import io.github.joaogouveia89.randomuser.core.model.User
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListGetState
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
import io.github.joaogouveia89.randomuser.userList.domain.repository.UsersDeleteState
import io.github.joaogouveia89.randomuser.userList.domain.source.UserListSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserListRepositoryImpl @Inject constructor(
    private val userListSource: UserListSource
) : UserListRepository {
    override suspend fun getUsers(): Flow<UserListGetState> = flow {
        emit(UserListGetState.Loading)

        val users = userListSource
            .getUsers()
            .sortedWith(
                compareBy(
                    User::firstName,
                    User::lastName
                )
            )

        emit(UserListGetState.Success(users))
    }

    override suspend fun deleteUsers(users: List<User>): Flow<UsersDeleteState> = flow {
        emit(UsersDeleteState.Loading)

        userListSource
            .deleteUsers(users)

        emit(UsersDeleteState.Success)
    }
}