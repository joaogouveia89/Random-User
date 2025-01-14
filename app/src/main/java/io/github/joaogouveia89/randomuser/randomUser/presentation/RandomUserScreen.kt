package io.github.joaogouveia89.randomuser.randomUser.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.toColorInt
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
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
    val user = uiState.user

    val iconsBackgroundColor = if (user.nationalityColors.first.isNotEmpty())
        Color(user.nationalityColors.first.toColorInt())
    else
        MaterialTheme.colorScheme.surface

    val iconsColor = if (user.nationalityColors.second.isNotEmpty())
        Color(user.nationalityColors.second.toColorInt())
    else
        MaterialTheme.colorScheme.surface

    if (uiState.isLoading || user == User()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        RandomUserContent(
            uiState = uiState,
            iconsBackgroundColor = iconsBackgroundColor,
            iconsColor = iconsColor,
            onAskNewUser = onAskNewUser,
            onOpenMapClick = onOpenMapClick,
            onAddToContactsClick = onAddToContactsClick,
            onCopyEmailToClipboard = onCopyEmailToClipboard,
            onDialRequired = onDialRequired,
            onCloseErrorBarClick = onCloseErrorBarClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
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