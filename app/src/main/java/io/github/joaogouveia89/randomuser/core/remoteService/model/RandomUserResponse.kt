package io.github.joaogouveia89.randomuser.core.remoteService.model

import com.google.gson.annotations.SerializedName


data class RandomUserResponse(
    @SerializedName("results") val results: List<Results>,
    @SerializedName("info") val info: Info
)