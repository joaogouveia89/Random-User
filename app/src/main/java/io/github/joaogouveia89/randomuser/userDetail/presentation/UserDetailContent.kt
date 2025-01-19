package io.github.joaogouveia89.randomuser.userDetail.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.presentation.components.user.UserDetails
import io.github.joaogouveia89.randomuser.userDetail.presentation.state.UserDetailState

@Composable
fun UserDetailContent(
    uiState: UserDetailState,
    onOpenMapClick: () -> Unit,
    onDeleteContactClick: () -> Unit,
    onCopyEmailToClipboard: () -> Unit,
    onDialRequired: (String) -> Unit,
) {
    UserDetails(
        user = uiState.user,
        locationTime = uiState.locationTime,
        onOpenMapClick = onOpenMapClick,
        onCopyEmailToClipboard = onCopyEmailToClipboard,
        onDialRequired = onDialRequired,
        detailsActionButton = {
            Button(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = Color.Red
                ),
                enabled = uiState.isDeleteButtonEnabled,
                onClick = onDeleteContactClick,
                shape = RoundedCornerShape(12.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.PersonOff,
                    contentDescription = "Delete user",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text =
                        stringResource(R.string.delete_contact)
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun UserDetailContentPreview() {
    UserDetailContent(
        uiState = UserDetailState(user = fakeUser),
        onOpenMapClick = {},
        onCopyEmailToClipboard =  {},
        onDialRequired =  {},
        onDeleteContactClick = {}
    )
}
