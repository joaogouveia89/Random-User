package io.github.joaogouveia89.randomuser.userList.presentation

import androidx.compose.runtime.Composable
import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.ContentContainer
import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState
import io.github.joaogouveia89.randomuser.core.model.User
import io.github.joaogouveia89.randomuser.userList.presentation.components.UserListContent
import io.github.joaogouveia89.randomuser.userList.presentation.state.UserListState

@Composable
fun UserListScreen(
    uiState: UserListState,
    onUserClick: (User) -> Unit,
    onUserLongClick: (User) -> Unit,
    onSearchQueryChange: (String) -> Unit,
) {
    ContentContainer(
        contentState = ContentState.Ready,
        showSnackBar = false,
        onSnackBarDismiss = {},
        onErrorRetry = {},
        content = {
            UserListContent(
                uiState = uiState,
                onUserClick = onUserClick,
                onUserLongClick = onUserLongClick,
                onSearchQueryChange = onSearchQueryChange
            )
        }
    )
}
