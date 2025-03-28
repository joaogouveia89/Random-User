package io.github.joaogouveia89.randomuser.core.model

import kotlinx.datetime.Instant

data class User(
    val id: Long = 0L,
    val gender: String = "",
    val title: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val countryCode: Country? = null,
    val latitude: String = "",
    val longitude: String = "",
    val timezoneOffset: String = "",
    val timezoneDescription: String = "",
    val email: String = "",
    val dateOfBirth: Instant? = null,
    val age: Int = 0,
    val phone: String = "",
    val cellPhone: String = "",
    val largePictureUrl: String = "",
    val thumbnailUrl: String = "",
    val nationality: Nationality? = null,
    val nationalityColors: Pair<String, String> = Pair("", "")
) {
    fun getMapsIntentQuery() =
        "geo:0,0?q=$latitude,$longitude($title+$firstName+$lastName+Location)"
}