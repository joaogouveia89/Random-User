package io.github.joaogouveia89.randomuser.userList.presentation.viewModel

import io.github.joaogouveia89.randomuser.core.model.User

sealed class UserListCommand {
    data object GetUsers : UserListCommand()
    data class UserClick(val user: User) : UserListCommand()
    data class UserLongClick(val user: User) : UserListCommand()
    data class Search(val query: String) : UserListCommand()
}