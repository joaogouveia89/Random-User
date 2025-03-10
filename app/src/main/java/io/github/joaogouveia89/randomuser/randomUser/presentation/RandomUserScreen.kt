package io.github.joaogouveia89.randomuser.randomUser.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.ContentContainer
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.UserProfileState
import io.github.joaogouveia89.randomuser.ui.theme.RandomUserTheme
import kotlinx.datetime.Clock

@Composable
fun RandomUserScreen(
    uiState: UserProfileState,
    onAskNewUser: () -> Unit,
    onOpenMapClick: () -> Unit,
    onAddToContactsClick: () -> Unit,
    onCopyEmailToClipboard: () -> Unit,
    onDialRequired: (phoneNumber: String) -> Unit,
    onCloseErrorBarClick: (() -> Unit)?,
    onErrorRetryClick: (() -> Unit)
) {
    ContentContainer(
        contentState = uiState.contentState,
        showSnackBar = uiState.showSnackBar,
        content = {
            RandomUserContent(
                uiState = uiState,
                onAskNewUser = onAskNewUser,
                onOpenMapClick = onOpenMapClick,
                onAddToContactsClick = onAddToContactsClick,
                onCopyEmailToClipboard = onCopyEmailToClipboard,
                onDialRequired = onDialRequired
            )
        },
        onSnackBarDismiss = onCloseErrorBarClick,
        onErrorRetry = onErrorRetryClick
    )
}

@Preview(showBackground = true)
@Composable
fun RandomUserScreenPreview() {
    RandomUserTheme {
        RandomUserScreen(
            uiState = UserProfileState(
                user = fakeUser,
                locationTime = Clock.System.now(),
                isReplacingUser = false
            ),
            onAskNewUser = {},
            onOpenMapClick = {},
            onAddToContactsClick = {},
            onCopyEmailToClipboard = {},
            onDialRequired = {},
            onErrorRetryClick = {},
            onCloseErrorBarClick = null
        )
    }
}