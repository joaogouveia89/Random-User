package io.github.joaogouveia89.randomuser.randomUser.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OfflinePin
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.presentation.components.ErrorSnackBar
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
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
    onCloseErrorBarClick: (() -> Unit)?
) {

    val showLoading = uiState.isLoading || uiState.user == User()

    val contentAlignment = if(showLoading)
        Alignment.Center
    else
        Alignment.TopStart

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = contentAlignment
    ){
        if(showLoading) CircularProgressIndicator()
        else{
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
        if(uiState.isOffline){
            ErrorSnackBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                icon = Icons.Outlined.WifiOff,
                messageRes = R.string.error_message_offline,
                onCloseErrorBarClick = {}
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
                isLoading = false
            ),
            onAskNewUser = {},
            onOpenMapClick = {},
            onAddToContactsClick = {},
            onCopyEmailToClipboard = {},
            onDialRequired = {},
            onCloseErrorBarClick = null
        )
    }
}