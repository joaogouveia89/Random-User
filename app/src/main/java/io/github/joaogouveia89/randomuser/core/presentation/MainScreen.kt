package io.github.joaogouveia89.randomuser.core.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.presentation.navigation.BottomNavBar
import io.github.joaogouveia89.randomuser.core.presentation.navigation.DetailScreenNav
import io.github.joaogouveia89.randomuser.core.presentation.navigation.NavigationGraph
import io.github.joaogouveia89.randomuser.core.presentation.navigation.currentRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            if (currentRoute(navHostController = navController) == DetailScreenNav.DetailScreen.route) {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = stringResource(R.string.user_details_title)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    navController.popBackStack()
                                },
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                )
            }
        },
        bottomBar = {
            if (currentRoute(navHostController = navController) != DetailScreenNav.DetailScreen.route) {
                BottomNavBar(navController = navController)
            }

        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavigationGraph(navController = navController)
            }
        }
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController())
}