package io.github.joaogouveia89.randomuser.userDetail.presentation.state

import androidx.annotation.StringRes
import io.github.joaogouveia89.randomuser.randomUser.domain.model.User
import kotlinx.datetime.Instant

data class UserDetailState(
    val user: User = User(),
    val locationTime: Instant? = null,
    val isLoading: Boolean = false,
    val isDeleteButtonEnabled: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val navigateBack: Boolean = false,
    @StringRes val errorMessage: Int? = null
)