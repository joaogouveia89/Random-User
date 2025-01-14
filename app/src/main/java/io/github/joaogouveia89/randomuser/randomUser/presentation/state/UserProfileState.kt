package io.github.joaogouveia89.randomuser.randomUser.presentation.state

import androidx.annotation.StringRes
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.datetime.Instant

data class UserProfileState(
    val user: User = User(),
    val locationTime: Instant? = null,
    val isLoading: Boolean = false,
    val isGettingNewUser: Boolean = false,
    @StringRes val errorMessage: Int? = null
) {
    val isSaveButtonEnabled: Boolean
        get() = user.id == 0L
}
