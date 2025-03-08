package io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel


sealed class UserDetailCommand {
    data object GetUserDetails : UserDetailCommand()
    data object DeleteUser : UserDetailCommand()
    data object DismissError : UserDetailCommand()
    data object DismissDeleteDialog : UserDetailCommand()
    data object ConfirmDeleteDialog : UserDetailCommand()
    data object OnLocalClockUpdated : UserDetailCommand()
}