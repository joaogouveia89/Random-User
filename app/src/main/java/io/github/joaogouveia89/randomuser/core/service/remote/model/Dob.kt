package io.github.joaogouveia89.randomuser.core.service.remote.model

import com.google.gson.annotations.SerializedName


data class Dob(
    @SerializedName("date") val date: String,
    @SerializedName("age") val age: Int
)