package io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel

import kotlinx.datetime.Clock


sealed class UserDetailCommand {
    data class GetUserDetails(val clock: Clock) : UserDetailCommand()
    data object DeleteUser : UserDetailCommand()
    data object DismissError : UserDetailCommand()
}