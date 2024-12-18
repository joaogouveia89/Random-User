package io.github.joaogouveia89.randomuser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.joaogouveia89.randomuser.data.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.data.UserSourceImpl
import io.github.joaogouveia89.randomuser.data.model.Country
import io.github.joaogouveia89.randomuser.data.model.Nationality
import io.github.joaogouveia89.randomuser.data.model.User
import io.github.joaogouveia89.randomuser.remoteService.RandomUserRetrofit
import io.github.joaogouveia89.randomuser.remoteService.model.Timezone
import io.github.joaogouveia89.randomuser.state.UserProfileState
import io.github.joaogouveia89.randomuser.ui.theme.RandomUserTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class MainActivity : ComponentActivity() {


    private val service by lazy { RandomUserRetrofit().service }
    private val userSource = UserSourceImpl(service)
    private val userRepository = UserRepositoryImpl(userSource)


    val viewModel = UserViewModel(userRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUserTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    val uiState by viewModel.uiState.collectAsState()
                    val user = uiState.user
                    UserProfileScreen(innerPadding, user, uiState){
                        val gmmIntentUri: Uri = Uri.parse("geo:${user.latitude},${user.longitude}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)
                    }
                }
            }
        }
    }
}

@Composable
private fun UserProfileScreen(
    innerPadding: PaddingValues,
    user: User,
    uiState: UserProfileState,
    onOpenMapClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
    ) {
        UserProfileHeader(
            title = user.title,
            firstName = user.firstName,
            lastName = user.lastName,
            pictureUrl = user.largePictureUrl,
            nationality = user.nationality?.reference ?: ""
        )
        UserLocation(
            city = user.city,
            state = user.state,
            country = user.country,
            countryCode = user.countryCode?.code ?: "",
            onOpenMapClick = onOpenMapClick
        )

        UserTimezone(
            timezone = user.timezoneOffset,
            timezoneDescription = user.timezoneDescription,
            localTime = uiState.locationTime
        )

        user.dateOfBirth?.let {
            Birthday(
                date = it,
                age = user.age
            )
        }

        Contacts(
            phone = user.phone,
            cellPhone = user.cellPhone,
            email = user.email
        )
    }
}


@Composable
fun UserProfileHeader(
    title: String,
    firstName: String,
    lastName: String,
    pictureUrl: String,
    nationality: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            model = pictureUrl,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            text = "$title $firstName $lastName"
        )

        AsyncImage(
            model = "https://flagsapi.com/$nationality/flat/64.png",
            contentDescription = null
        )
    }

}

@Composable
private fun UserLocation(
    city: String,
    state: String,
    country: String,
    countryCode: String,
    onOpenMapClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null
        )
        Column {
            Text("$city - $state")
            Icon(
                modifier = Modifier
                    .clickable {
                        onOpenMapClick()
                    },
                imageVector = Icons.Default.Map,
                contentDescription = null
            )
            Row {
                Text(country)
                AsyncImage(
                    model = "https://flagsapi.com/$countryCode/flat/64.png",
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun UserTimezone(
    timezone: String,
    timezoneDescription: String,
    localTime: Instant?,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = null
        )

        Column {
            Text(timezone)
            Text(timezoneDescription)
            val instant = localTime?.toLocalDateTime(TimeZone.currentSystemDefault())
            Text("${instant?.hour}:${instant?.minute}")
        }
    }
}

@Composable
fun Birthday(
    modifier: Modifier = Modifier,
    date: Instant,
    age: Int
) {

        val localTime = date.toLocalDateTime(TimeZone.UTC)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Cake,
                contentDescription = null
            )

            Column {
                Text("${localTime.dayOfMonth}/${localTime.monthNumber}/${localTime.year}")
                Text("$age")
            }
        }
}

@Composable
fun Contacts(
    modifier: Modifier = Modifier,
    phone: String,
    cellPhone: String,
    email: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = null
        )

        Column {
            Text(phone)
            Text(cellPhone)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Mail,
            contentDescription = null
        )

        Text(email)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RandomUserTheme {
        UserProfileScreen(
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