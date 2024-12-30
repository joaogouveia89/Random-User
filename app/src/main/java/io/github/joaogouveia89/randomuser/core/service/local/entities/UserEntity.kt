package io.github.joaogouveia89.randomuser.core.service.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
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
    val nationalityReference: String?
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
    nationalityReference = nationality?.reference
)