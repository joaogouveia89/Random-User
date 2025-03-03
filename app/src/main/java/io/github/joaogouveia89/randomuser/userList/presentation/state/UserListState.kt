package io.github.joaogouveia89.randomuser.userList.presentation.state

import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User

data class UserListState(
    val contentState: ContentState = ContentState.Ready,
    val userList: List<User> = listOf(),
    val showSnackBar: Boolean = false
) {
    val hasNoUser: Boolean
        get() = userList.isEmpty()
}