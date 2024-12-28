package io.github.joaogouveia89.randomuser.core.fakeData

import io.github.joaogouveia89.randomuser.randomUser.domain.model.Country
import io.github.joaogouveia89.randomuser.randomUser.domain.model.Nationality
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.datetime.Instant

val fakeUser = User(
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
)