package io.github.joaogouveia89.randomuser.core.service.remote.model

import com.google.gson.annotations.SerializedName


data class Location(
    @SerializedName("street") val street: io.github.joaogouveia89.randomuser.core.service.remote.model.Street,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("postcode") val postcode: String,
    @SerializedName("coordinates") val coordinates: io.github.joaogouveia89.randomuser.core.service.remote.model.Coordinates,
    @SerializedName("timezone") val timezone: io.github.joaogouveia89.randomuser.core.service.remote.model.Timezone
)