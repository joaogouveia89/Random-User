package io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel

sealed class RandomUserCommand {
    data object MonitorInternetStatus : RandomUserCommand()
    data object GetNewUser : RandomUserCommand()
    data object SaveUser : RandomUserCommand()
    data object DismissError : RandomUserCommand()
    data object ErrorRetryClick : RandomUserCommand()
    data object OnLocalClockUpdated : RandomUserCommand()
}