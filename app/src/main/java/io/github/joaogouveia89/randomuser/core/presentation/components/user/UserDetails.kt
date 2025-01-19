package io.github.joaogouveia89.randomuser.core.presentation.components.user

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
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import io.github.joaogouveia89.randomuser.core.presentation.components.CardSection
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun UserDetails(
    modifier: Modifier = Modifier,
    user: User,
    locationTime: Instant?,
    detailsActionButton: @Composable () -> Unit,
    onOpenMapClick: () -> Unit,
    onCopyEmailToClipboard: () -> Unit,
    onDialRequired: (String) -> Unit,
) {
    val iconsBackgroundColor = if (user.nationalityColors.first.isNotEmpty())
        Color(user.nationalityColors.first.toColorInt())
    else
        MaterialTheme.colorScheme.surface

    val iconsColor = if (user.nationalityColors.second.isNotEmpty())
        Color(user.nationalityColors.second.toColorInt())
    else
        MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier
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
                localTime = locationTime?.humanizedHourMin() ?: "",
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
            detailsActionButton()
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Preview
@Composable
private fun UserDetailsPreview() {
    UserDetails(
        user = fakeUser,
        onCopyEmailToClipboard = {},
        onDialRequired = {},
        onOpenMapClick = {},
        locationTime = Clock.System.now(),
        detailsActionButton = {
            Button(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                enabled = true,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add to Contacts",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text =
                    stringResource(R.string.add_to_contacts)
                )
            }
        }
    )
}