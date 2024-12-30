package io.github.joaogouveia89.randomuser.core.service.remote.model

import com.google.gson.annotations.SerializedName


data class RandomUserResponse(
    @SerializedName("results") val results: List<io.github.joaogouveia89.randomuser.core.service.remote.model.Results>,
    @SerializedName("info") val info: io.github.joaogouveia89.randomuser.core.service.remote.model.Info
)