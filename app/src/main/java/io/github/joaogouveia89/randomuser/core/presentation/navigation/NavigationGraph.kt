package io.github.joaogouveia89.randomuser.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.github.joaogouveia89.randomuser.randomUser.copyEmailToClipboard
import io.github.joaogouveia89.randomuser.randomUser.dial
import io.github.joaogouveia89.randomuser.randomUser.openMaps
import io.github.joaogouveia89.randomuser.randomUser.presentation.RandomUserScreen
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserCommand
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserViewModel
import io.github.joaogouveia89.randomuser.userDetail.presentation.UserDetailScreen
import io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel.UserDetailCommand
import io.github.joaogouveia89.randomuser.userDetail.presentation.viewModel.UserDetailViewModel
import io.github.joaogouveia89.randomuser.userList.presentation.UserListScreen
import io.github.joaogouveia89.randomuser.userList.presentation.viewModel.UserListCommand
import io.github.joaogouveia89.randomuser.userList.presentation.viewModel.UserListViewModel
import kotlinx.datetime.Clock


@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.RANDOM_USER_ROUTE
    ) {

        composable(BottomNavItem.RandomUser.route) {
            val context = LocalContext.current

            val viewModel: RandomUserViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.execute(RandomUserCommand.GetNewUser)
            }

            RandomUserScreen(
                uiState = uiState,
                onAskNewUser = { viewModel.execute(RandomUserCommand.GetNewUser) },
                onOpenMapClick = { context.openMaps(uiState.user.getMapsIntentQuery()) },
                onAddToContactsClick = { viewModel.execute(RandomUserCommand.SaveUser) },
                onCopyEmailToClipboard = { context.copyEmailToClipboard(uiState.user.email) },
                onDialRequired = { context.dial(it) },
                onCloseErrorBarClick = { viewModel.execute(RandomUserCommand.DismissError) },
                onErrorRetryClick = { viewModel.execute(RandomUserCommand.ErrorRetryClick) }
            )
        }
        composable(BottomNavItem.UserList.route) {
            val viewModel: UserListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.execute(UserListCommand.GetUsers)
            }

            UserListScreen(
                uiState = uiState,
                onUserClick = {
                    navController.navigate(DetailScreenNav.DetailScreen.passUserId(userId = it.id))
                },
                onSearchQueryChange = {
                    viewModel.execute(UserListCommand.Search(it))
                }
            )
        }

        composable(
            DetailScreenNav.DetailScreen.route,
            arguments = listOf(
                navArgument(DetailScreenNav.USER_ID) {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) {
            val viewModel: UserDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                viewModel.execute(UserDetailCommand.GetUserDetails(Clock.System))
            }

            LaunchedEffect(uiState.navigateBack) {
                if (uiState.navigateBack) {
                    navController.popBackStack()
                }
            }

            UserDetailScreen(
                uiState = uiState,
                onOpenMapClick = {},
                onDeleteContactClick = { viewModel.execute(UserDetailCommand.DeleteUser) },
                onCopyEmailToClipboard = {},
                onDialRequired = {},
                onDeleteDialogConfirmation = { viewModel.execute(UserDetailCommand.ConfirmDeleteDialog) },
                onDeleteDialogDismiss = { viewModel.execute(UserDetailCommand.DismissDeleteDialog) },
            )
        }
    }
}