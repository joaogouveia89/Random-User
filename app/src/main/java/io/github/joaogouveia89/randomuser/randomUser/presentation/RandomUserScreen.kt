package io.github.joaogouveia89.randomuser.randomUser.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.fakeData.fakeUser
import io.github.joaogouveia89.randomuser.core.ktx.humanizedHourMin
import io.github.joaogouveia89.randomuser.randomUser.presentation.components.UserBirthday
import io.github.joaogouveia89.randomuser.randomUser.presentation.components.UserContacts
import io.github.joaogouveia89.randomuser.randomUser.presentation.components.UserLocation
import io.github.joaogouveia89.randomuser.randomUser.presentation.components.UserProfileHeader
import io.github.joaogouveia89.randomuser.randomUser.presentation.components.UserTimezone
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
    onDialRequired: (phoneNumber: String) -> Unit
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

    if (uiState.isLoading) {
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
            onDialRequired = onDialRequired
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomUserContent(
    uiState: UserProfileState,
    iconsBackgroundColor: Color,
    iconsColor: Color,
    onAskNewUser: () -> Unit,
    onOpenMapClick: () -> Unit,
    onAddToContactsClick: () -> Unit,
    onCopyEmailToClipboard: () -> Unit,
    onDialRequired: (String) -> Unit
) {
    val user = uiState.user
    PullToRefreshBox(
        isRefreshing = uiState.isGettingNewUser,
        onRefresh = onAskNewUser,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            UserProfileHeader(
                title = user.title,
                firstName = user.firstName,
                lastName = user.lastName,
                pictureUrl = user.largePictureUrl,
                nationality = user.nationality?.reference ?: ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardSection {
                UserLocation(
                    city = user.city,
                    state = user.state,
                    country = user.country,
                    iconBackgroundColor = iconsBackgroundColor,
                    iconColor = iconsColor,
                    onOpenMapClick = onOpenMapClick
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CardSection {
                UserTimezone(
                    timezone = user.timezoneOffset,
                    timezoneDescription = user.timezoneDescription,
                    localTime = uiState.locationTime?.humanizedHourMin() ?: "",
                    iconBackgroundColor = iconsBackgroundColor,
                    iconColor = iconsColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            user.dateOfBirth?.let {
                CardSection {
                    UserBirthday(
                        date = it,
                        age = user.age,
                        iconBackgroundColor = iconsBackgroundColor,
                        iconColor = iconsColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CardSection {
                UserContacts(
                    phone = user.phone,
                    cellPhone = user.cellPhone,
                    email = user.email,
                    iconBackgroundColor = iconsBackgroundColor,
                    iconColor = iconsColor,
                    onCopyEmailToClipboard = onCopyEmailToClipboard,
                    onDialRequired = onDialRequired
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add to Contacts Button
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

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

            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}

@Composable
private fun CardSection(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        content()
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
            onDialRequired = {}
        )
    }
}