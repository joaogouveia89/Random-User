package io.github.joaogouveia89.randomuser.core.service.remote.model

import com.google.gson.annotations.SerializedName


data class Results(
    @SerializedName("gender") val gender: String,
    @SerializedName("name") val name: io.github.joaogouveia89.randomuser.core.service.remote.model.Name,
    @SerializedName("location") val location: io.github.joaogouveia89.randomuser.core.service.remote.model.Location,
    @SerializedName("email") val email: String,
    @SerializedName("login") val login: io.github.joaogouveia89.randomuser.core.service.remote.model.Login,
    @SerializedName("dob") val dob: io.github.joaogouveia89.randomuser.core.service.remote.model.Dob,
    @SerializedName("registered") val registered: io.github.joaogouveia89.randomuser.core.service.remote.model.Registered,
    @SerializedName("phone") val phone: String,
    @SerializedName("cell") val cell: String,
    @SerializedName("id") val id: io.github.joaogouveia89.randomuser.core.service.remote.model.Id,
    @SerializedName("picture") val picture: io.github.joaogouveia89.randomuser.core.service.remote.model.Picture,
    @SerializedName("nat") val nat: String
)