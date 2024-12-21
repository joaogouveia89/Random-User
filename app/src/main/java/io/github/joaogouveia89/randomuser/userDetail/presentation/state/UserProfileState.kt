package io.github.joaogouveia89.randomuser.userDetail.presentation.state

import io.github.joaogouveia89.randomuser.domain.model.User
import kotlinx.datetime.Instant

data class UserProfileState(
    val user: User = User(),
    val locationTime: Instant? = null,
    val isLoading: Boolean = true
)
