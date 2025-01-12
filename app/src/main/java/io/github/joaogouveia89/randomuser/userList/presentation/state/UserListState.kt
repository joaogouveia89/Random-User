package io.github.joaogouveia89.randomuser.userList.presentation.state

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User

data class UserListState(
    val isLoading: Boolean = false,
    val userList: List<User> = listOf(),
    val isError: Boolean = false
){
    val hasNoUser: Boolean
        get() = userList.isEmpty()
}