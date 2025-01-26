package io.github.joaogouveia89.randomuser.core.presentation.navigation

import io.github.joaogouveia89.randomuser.core.presentation.navigation.Routes.USER_DETAIL_ROUTE

sealed class DetailScreenNav(val route: String) {
    data object DetailScreen : DetailScreenNav(
        route = "$USER_DETAIL_ROUTE?$USER_ID=" +
                "{$USER_ID}"
    ) {
        fun passUserId(userId: Long) =
            "$USER_DETAIL_ROUTE?$USER_ID=$userId"
    }

    companion object {
        const val USER_ID = "userId"
    }
}