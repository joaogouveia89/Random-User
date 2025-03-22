package io.github.joaogouveia89.randomuser.userList.presentation.state

import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState
import io.github.joaogouveia89.randomuser.core.model.User

data class UserListState(
    val contentState: ContentState = ContentState.Ready,
    val userList: List<Pair<User, Boolean>> = listOf(),
    val showSnackBar: Boolean = false,
    val isMultiSelectionMode: Boolean = false,
    val askedForDetails: User? = null
) {
    val hasNoUser: Boolean
        get() = userList.isEmpty()
}