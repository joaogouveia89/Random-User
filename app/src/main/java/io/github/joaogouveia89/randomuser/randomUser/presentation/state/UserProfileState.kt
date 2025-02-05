package io.github.joaogouveia89.randomuser.randomUser.presentation.state

import androidx.annotation.StringRes
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.datetime.Instant

enum class LoadState {
    GETTING_USER, REPLACING_USER, IDLE
}

sealed class ErrorState {
    data object None : ErrorState()
    data object Offline : ErrorState()
    data object FullScreenError : ErrorState()
    data class SnackBarError(@StringRes val message: Int) : ErrorState()
}

data class UserProfileState(
    val user: User = User(),
    val locationTime: Instant? = null,
    val loadState: LoadState = LoadState.IDLE,
    val isSaving: Boolean = false,
    val errorState: ErrorState = ErrorState.None
) {
    val isSaveButtonEnabled: Boolean
        get() = user.id == 0L
}
