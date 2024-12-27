package io.github.joaogouveia89.randomuser.userDetail.presentation.state

import io.github.joaogouveia89.randomuser.userDetail.domain.model.User

data class UserProfileState(
    val user: User = User(),
    val locationTime: String = "",
    val isLoading: Boolean = true,
    val isGettingNewUser: Boolean = false,
)
