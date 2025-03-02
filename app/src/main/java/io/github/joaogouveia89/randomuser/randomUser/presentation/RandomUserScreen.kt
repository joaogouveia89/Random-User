package io.github.joaogouveia89.randomuser.randomUser.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.presentation.screen.GenericErrorScreen
import io.github.joaogouveia89.randomuser.core.presentation.screen.OfflineScreen
import io.github.joaogouveia89.randomuser.core.presentation.components.ErrorSnackBar
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.ErrorState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.LoadState
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

    val contentAlignment = if (uiState.loadState == LoadState.GETTING_USER)
        Alignment.Center
    else
        Alignment.TopStart

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = contentAlignment
    ) {
        if(uiState.errorState == ErrorState.OfflineScreen) OfflineScreen()
        if(uiState.errorState == ErrorState.ScreenError) GenericErrorScreen(onRetry = onErrorRetryClick)
        else if (uiState.loadState == LoadState.GETTING_USER) CircularProgressIndicator()
        else {
            RandomUserContent(
                uiState = uiState,
                onAskNewUser = onAskNewUser,
                onOpenMapClick = onOpenMapClick,
                onAddToContactsClick = onAddToContactsClick,
                onCopyEmailToClipboard = onCopyEmailToClipboard,
                onDialRequired = onDialRequired,
                onCloseErrorBarClick = onCloseErrorBarClick
            )
        }
        if (uiState.errorState is ErrorState.OfflineSnackBar) {
            ErrorSnackBar(
                modifier = Modifier.align(Alignment.TopCenter),
                icon = Icons.Outlined.WifiOff,
                messageRes = R.string.error_message_offline
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RandomUserScreenPreview() {
    RandomUserTheme {
        RandomUserScreen(
            uiState = UserProfileState(
                user = fakeUser,
                locationTime = Clock.System.now(),
                loadState = LoadState.IDLE
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