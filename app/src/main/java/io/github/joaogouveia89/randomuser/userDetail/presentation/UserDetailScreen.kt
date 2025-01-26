package io.github.joaogouveia89.randomuser.userDetail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import io.github.joaogouveia89.randomuser.userDetail.presentation.state.UserDetailState

@Composable
fun UserDetailScreen(
    uiState: UserDetailState,
    onOpenMapClick: () -> Unit,
    onDeleteContactClick: () -> Unit,
    onCopyEmailToClipboard: () -> Unit,
    onDialRequired: (String) -> Unit,
    onDeleteDialogConfirmation: () -> Unit,
    onDeleteDialogDismiss: () -> Unit,
) {
    if (uiState.isLoading || uiState.user == User()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if (uiState.showDeleteDialog) {
            DeleteConfirmationDialog(
                onConfirmation = { onDeleteDialogConfirmation.invoke() },
                onDismissRequest = { onDeleteDialogDismiss.invoke() }
            )
        }

        UserDetailContent(
            uiState = uiState,
            onOpenMapClick = onOpenMapClick,
            onDeleteContactClick = onDeleteContactClick,
            onCopyEmailToClipboard = onCopyEmailToClipboard,
            onDialRequired = onDialRequired
        )
    }
}