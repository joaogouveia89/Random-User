package io.github.joaogouveia89.randomuser.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.joaogouveia89.randomuser.randomUser.copyEmailToClipboard
import io.github.joaogouveia89.randomuser.randomUser.dial
import io.github.joaogouveia89.randomuser.randomUser.openMaps
import io.github.joaogouveia89.randomuser.randomUser.presentation.RandomUserScreen
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserCommand
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserViewModel
import kotlinx.datetime.Clock


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.RandomUser.route
    ) {

        composable(BottomNavItem.RandomUser.route) {
            val context = LocalContext.current

            val viewModel: RandomUserViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.execute(RandomUserCommand.GetNewUser(Clock.System))
            }

            RandomUserScreen(
                uiState = uiState,
                onAskNewUser = { viewModel.execute(RandomUserCommand.GetNewUser(Clock.System)) },
                onOpenMapClick = { context.openMaps(uiState.user.getMapsIntentQuery()) },
                onAddToContactsClick = { viewModel.execute(RandomUserCommand.SaveUser) },
                onCopyEmailToClipboard = { context.copyEmailToClipboard(uiState.user.email) },
                onDialRequired = { context.dial(it) }
            )
        }
        composable(BottomNavItem.UserList.route) {

        }
    }
}