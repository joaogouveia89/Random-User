package io.github.joaogouveia89.randomuser.remoteService.model

import com.google.gson.annotations.SerializedName


data class Coordinates(
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String
)