package io.github.joaogouveia89.randomuser.userList.data.repository

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListGetState
import io.github.joaogouveia89.randomuser.userList.domain.repository.UserListRepository
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
}