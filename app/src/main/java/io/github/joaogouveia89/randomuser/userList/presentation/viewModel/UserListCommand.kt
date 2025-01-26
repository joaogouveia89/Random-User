package io.github.joaogouveia89.randomuser.userList.presentation.viewModel

sealed class UserListCommand {
    data object GetUsers : UserListCommand()
}