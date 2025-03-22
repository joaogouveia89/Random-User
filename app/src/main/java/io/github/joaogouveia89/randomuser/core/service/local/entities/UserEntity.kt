package io.github.joaogouveia89.randomuser.core.service.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.joaogouveia89.randomuser.core.model.Country
import io.github.joaogouveia89.randomuser.core.model.Nationality
import io.github.joaogouveia89.randomuser.core.model.User
import kotlinx.datetime.Instant

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val gender: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val state: String,
    val country: String,
    val countryCode: String? = null,
    val latitude: String,
    val longitude: String,
    val timezoneOffset: String,
    val timezoneDescription: String,
    val email: String,
    val dateOfBirth: Instant? = null,
    val age: Int = 0,
    val phone: String,
    val cellPhone: String,
    val largePictureUrl: String,
    val thumbnailUrl: String,
    val nationalityReference: String?,
    val nationalityColor1: String,
    val nationalityColor2: String
)

fun User.asEntity() = UserEntity(
    id = id,
    gender = gender,
    title = title,
    firstName = firstName,
    lastName = lastName,
    city = city,
    state = state,
    country = country,
    countryCode = countryCode?.code,
    latitude = latitude,
    longitude = longitude,
    timezoneOffset = timezoneOffset,
    timezoneDescription = timezoneDescription,
    email = email,
    dateOfBirth = dateOfBirth,
    age = age,
    phone = phone,
    cellPhone = cellPhone,
    largePictureUrl = largePictureUrl,
    thumbnailUrl = thumbnailUrl,
    nationalityReference = nationality?.reference,
    nationalityColor1 = nationalityColors.first,
    nationalityColor2 = nationalityColors.second,
)

fun UserEntity.asUser() = User(
    id = id,
    gender = gender,
    title = title,
    firstName = firstName,
    lastName = lastName,
    city = city,
    state = state,
    country = country,
    countryCode = Country.getByName(countryCode),
    latitude = latitude,
    longitude = longitude,
    timezoneOffset = timezoneOffset,
    timezoneDescription = timezoneDescription,
    email = email,
    dateOfBirth = dateOfBirth,
    age = age,
    phone = phone,
    cellPhone = cellPhone,
    largePictureUrl = largePictureUrl,
    thumbnailUrl = thumbnailUrl,
    nationality = Nationality.fromReference(nationalityReference),
    nationalityColors = Pair(nationalityColor1, nationalityColor2)
)

fun List<UserEntity>.asUsers() = map { it.asUser() }