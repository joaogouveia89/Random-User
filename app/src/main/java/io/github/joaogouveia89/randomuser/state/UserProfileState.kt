package io.github.joaogouveia89.randomuser.state

import io.github.joaogouveia89.randomuser.data.model.User
import kotlinx.datetime.Instant

data class UserProfileState(
    val user: User = User(),
    val locationTime: Instant? = null,
    val isLoading: Boolean = false
)
