package io.github.joaogouveia89.randomuser.core.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.Person3
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.joaogouveia89.randomuser.R

sealed class BottomNavItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
) {
    data object RandomUser : BottomNavItem(
        title = R.string.random_user,
        icon = Icons.Default.Person3,
        route = "route_random_user"
    )

    data object UserList : BottomNavItem(
        title = R.string.user_list,
        icon = Icons.Default.ImportContacts,
        route = "route_user_list"
    )
}