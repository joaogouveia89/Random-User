package io.github.joaogouveia89.randomuser.userDetail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joaogouveia89.randomuser.data.model.Country
import io.github.joaogouveia89.randomuser.data.model.Nationality
import io.github.joaogouveia89.randomuser.data.model.User
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
    onOpenMapClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        UserProfileHeader(
            title = user.title,
            firstName = user.firstName,
            lastName = user.lastName,
            pictureUrl = user.largePictureUrl,
            nationality = user.nationality?.reference ?: ""
        )
        Spacer(modifier = Modifier.height(16.dp))
        UserLocation(
            city = user.city,
            state = user.state,
            country = user.country,
            countryCode = user.countryCode?.code ?: "",
            onOpenMapClick = onOpenMapClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        UserTimezone(
            timezone = user.timezoneOffset,
            timezoneDescription = user.timezoneDescription,
            localTime = uiState.locationTime
        )
        Spacer(modifier = Modifier.height(16.dp))
        user.dateOfBirth?.let {
            UserBirthday(
                date = it,
                age = user.age
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        UserContacts(
            phone = user.phone,
            cellPhone = user.cellPhone,
            email = user.email
        )
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
            onOpenMapClick = { /* Do nothing for preview */ }
        )
    }
}