package io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel

import kotlinx.datetime.Clock

sealed class RandomUserCommand {
    data class GetNewUser(val clock: Clock) : RandomUserCommand()
    data object SaveUser : RandomUserCommand()
    data object DismissError : RandomUserCommand()
}