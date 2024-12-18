package io.github.joaogouveia89.randomuser.remoteService.model.mappers

import io.github.joaogouveia89.randomuser.data.model.Country
import io.github.joaogouveia89.randomuser.data.model.Nationality
import io.github.joaogouveia89.randomuser.data.model.User
import io.github.joaogouveia89.randomuser.remoteService.model.Results
import kotlinx.datetime.Instant

fun Results.asUser(): User =
    User(
        gender = gender,
        title = name.title,
        firstName = name.first,
        lastName = name.last,
        city = location.city,
        state = location.state,
        country = location.country,
        countryCode = Country.getByName(location.country),
        latitude = location.coordinates.latitude,
        longitude = location.coordinates.longitude,
        timezoneOffset = location.timezone.offset,
        timezoneDescription = location.timezone.description,
        email = email,
        dateOfBirth = Instant.parse(dob.date),
        age = dob.age,
        phone = phone,
        cellPhone = cell,
        largePictureUrl = picture.large,
        thumbnailUrl = picture.thumbnail,
        nationality = Nationality.fromReference(nat)
    )