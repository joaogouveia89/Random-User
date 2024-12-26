package io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel

sealed class RandomUserCommand {
    data object GetNewUser : RandomUserCommand()
}