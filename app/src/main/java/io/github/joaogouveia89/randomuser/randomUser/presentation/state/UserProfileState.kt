package io.github.joaogouveia89.randomuser.randomUser.presentation.state

import io.github.joaogouveia89.randomuser.randomUser.domain.model.User

data class UserProfileState(
    val user: User = User(),
    val locationTime: String = "",
    val isLoading: Boolean = true,
    val isGettingNewUser: Boolean = false
) {
    val isSaveButtonEnabled: Boolean
        get() = user.id == 0L
}
