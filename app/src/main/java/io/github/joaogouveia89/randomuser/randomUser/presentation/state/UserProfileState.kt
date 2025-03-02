package io.github.joaogouveia89.randomuser.randomUser.presentation.state

import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.datetime.Instant

enum class LoadState {
    GETTING_USER, REPLACING_USER, IDLE
}

data class UserProfileState(
    val user: User = User(),
    val locationTime: Instant? = null,
    val loadState: LoadState = LoadState.IDLE,
    val isSaving: Boolean = false,
    val contentState: ContentState = ContentState.Ready,
    val showSnackBar: Boolean = false
) {
    val isSaveButtonEnabled: Boolean
        get() = user.id == 0L

    fun shouldShowFullErrorScreen(): Boolean = user == User()
}
