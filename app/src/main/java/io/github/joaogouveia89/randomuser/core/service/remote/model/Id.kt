package io.github.joaogouveia89.randomuser.core.service.remote.model

import com.google.gson.annotations.SerializedName


data class Id(
    @SerializedName("name") val name: String,
    @SerializedName("value") val value: String
)