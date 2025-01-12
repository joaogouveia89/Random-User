package io.github.joaogouveia89.randomuser.userList.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.userList.presentation.components.UserListContent
import io.github.joaogouveia89.randomuser.userList.presentation.state.UserListState

@Composable
fun UserListScreen(
    uiState: UserListState,
    onUserClick: (User) -> Unit
) {
    if (uiState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        UserListContent(
            uiState = uiState,
            onUserClick = onUserClick
        )
    }
}
