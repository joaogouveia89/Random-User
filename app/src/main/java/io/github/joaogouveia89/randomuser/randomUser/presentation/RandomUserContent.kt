package io.github.joaogouveia89.randomuser.randomUser.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.presentation.components.ErrorSnackBar
import io.github.joaogouveia89.randomuser.core.presentation.components.user.UserDetails
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.ErrorState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.LoadState
import io.github.joaogouveia89.randomuser.randomUser.presentation.state.UserProfileState
import io.github.joaogouveia89.randomuser.ui.theme.RandomUserTheme
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomUserContent(
    uiState: UserProfileState,
    onAskNewUser: () -> Unit,
    onOpenMapClick: () -> Unit,
    onAddToContactsClick: () -> Unit,
    onCopyEmailToClipboard: () -> Unit,
    onDialRequired: (String) -> Unit,
    onCloseErrorBarClick: (() -> Unit)?
) {
    PullToRefreshBox(
        isRefreshing = uiState.loadState == LoadState.REPLACING_USER,
        onRefresh = onAskNewUser,
    ) {
        UserDetails(
            user = uiState.user,
            locationTime = uiState.locationTime,
            onOpenMapClick = onOpenMapClick,
            onCopyEmailToClipboard = onCopyEmailToClipboard,
            onDialRequired = onDialRequired,
            detailsActionButton = {
                with(uiState.isSaveButtonEnabled) {
                    val isEnabled = this
                    Button(
                        onClick = onAddToContactsClick,
                        shape = RoundedCornerShape(12.dp),
                        enabled = isEnabled,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (isEnabled) Icons.Default.PersonAdd else Icons.Default.CheckCircleOutline,
                            contentDescription = if (isEnabled) "Add to Contacts" else "Added to Contacts",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnabled)
                                stringResource(R.string.add_to_contacts)
                            else
                                stringResource(R.string.added_to_contacts)
                        )
                    }
                }
            }
        )

        if (uiState.errorState is ErrorState.SnackBarError) {
            ErrorSnackBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                messageRes = uiState.errorState.message,
                onCloseErrorBarClick = onCloseErrorBarClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RandomUserContentPreview() {
    RandomUserTheme {
        RandomUserContent(
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
            onCloseErrorBarClick = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RandomUserContentWithErrorPreview() {
    RandomUserTheme {
        RandomUserContent(
            uiState = UserProfileState(
                user = fakeUser,
                locationTime = Clock.System.now(),
                loadState = LoadState.IDLE,
                errorState = ErrorState.SnackBarError(R.string.error_message_source)
            ),
            onAskNewUser = {},
            onOpenMapClick = {},
            onAddToContactsClick = {},
            onCopyEmailToClipboard = {},
            onDialRequired = {},
            onCloseErrorBarClick = {}
        )
    }
}