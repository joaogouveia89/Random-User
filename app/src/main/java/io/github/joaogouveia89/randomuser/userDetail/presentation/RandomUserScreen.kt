package io.github.joaogouveia89.randomuser.userDetail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import io.github.joaogouveia89.randomuser.domain.model.Country
import io.github.joaogouveia89.randomuser.domain.model.Nationality
import io.github.joaogouveia89.randomuser.domain.model.User
import io.github.joaogouveia89.randomuser.ui.theme.RandomUserTheme
import io.github.joaogouveia89.randomuser.userDetail.presentation.components.UserBirthday
import io.github.joaogouveia89.randomuser.userDetail.presentation.components.UserContacts
import io.github.joaogouveia89.randomuser.userDetail.presentation.components.UserLocation
import io.github.joaogouveia89.randomuser.userDetail.presentation.components.UserProfileHeader
import io.github.joaogouveia89.randomuser.userDetail.presentation.components.UserTimezone
import io.github.joaogouveia89.randomuser.userDetail.presentation.state.UserProfileState
import kotlinx.datetime.Instant

@Composable
fun RandomUserScreen(
    innerPadding: PaddingValues,
    user: User,
    uiState: UserProfileState,
    onOpenMapClick: () -> Unit,
    onAddToContactsClick: () -> Unit
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
        modifier = Modifier
            .padding(innerPadding)
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
                localTime = uiState.locationTime,
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
                iconColor = iconsColor
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Add to Contacts Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onAddToContactsClick,
                shape = RoundedCornerShape(12.dp),
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
                Text("Add to Contacts")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

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
        //colors = CardDefaults.cardColors(containerColor = )
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomUserTheme {
        RandomUserScreen(
            innerPadding = PaddingValues(16.dp),
            user = User(
                title = "Mr.",
                firstName = "John",
                lastName = "Doe",
                largePictureUrl = "https://randomuser.me/api/portraits/men/1.jpg",
                nationality = Nationality.US,
                city = "New York",
                state = "New York",
                country = "United States",
                countryCode = Country.UNITED_STATES,
                latitude = "40.7128",
                longitude = "-74.0060",
                timezoneOffset = "UTC-5",
                timezoneDescription = "Eastern Standard Time",
                dateOfBirth = Instant.parse("1990-05-20T00:00:00Z"),
                age = 34,
                phone = "(555) 123-4567",
                cellPhone = "(555) 987-6543",
                email = "johndoe@example.com"
            ),
            uiState = UserProfileState(
                user = User(
                    title = "Mr.",
                    firstName = "John",
                    lastName = "Doe",
                    largePictureUrl = "https://randomuser.me/api/portraits/men/1.jpg",
                    nationality = Nationality.US,
                    city = "New York",
                    state = "New York",
                    country = "United States",
                    countryCode = Country.UNITED_STATES,
                    latitude = "40.7128",
                    longitude = "-74.0060",
                    timezoneOffset = "UTC-5",
                    timezoneDescription = "Eastern Standard Time",
                    dateOfBirth = Instant.parse("1990-05-20T00:00:00Z"),
                    age = 34,
                    phone = "(555) 123-4567",
                    cellPhone = "(555) 987-6543",
                    email = "johndoe@example.com"
                ),
                locationTime = Instant.parse("2024-04-01T14:30:00Z")
            ),
            onOpenMapClick = { /* Do nothing for preview */ },
            onAddToContactsClick = {}
        )
    }
}