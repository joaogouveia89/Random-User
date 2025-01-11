package io.github.joaogouveia89.randomuser.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.joaogouveia89.randomuser.core.presentation.navigation.BottomNavBar
import io.github.joaogouveia89.randomuser.core.presentation.navigation.NavigationGraph

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
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