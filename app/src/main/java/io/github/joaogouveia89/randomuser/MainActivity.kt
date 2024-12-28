package io.github.joaogouveia89.randomuser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.joaogouveia89.randomuser.core.remoteService.RandomUserRetrofit
import io.github.joaogouveia89.randomuser.randomUser.data.repository.UserRepositoryImpl
import io.github.joaogouveia89.randomuser.ui.theme.RandomUserTheme
import io.github.joaogouveia89.randomuser.randomUser.data.source.UserSourceImpl
import io.github.joaogouveia89.randomuser.randomUser.presentation.RandomUserScreen
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserCommand
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserViewModel


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val service by lazy { RandomUserRetrofit().service }
    private val userSource = UserSourceImpl(service)
    private val userRepository = UserRepositoryImpl(userSource)
    val viewModel = RandomUserViewModel(userRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RandomUserTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    PullToRefreshBox(
                        isRefreshing = uiState.isGettingNewUser,
                        onRefresh = {
                            viewModel.execute(RandomUserCommand.GetNewUser)
                        },
                    ) {
                        RandomUserScreen(
                            innerPadding,
                            uiState,
                            onAddToContactsClick = {},
                            onOpenMapClick = {
                                val gmmIntentUri: Uri =
                                    Uri.parse(uiState.user.getMapsIntentQuery())
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                startActivity(mapIntent)
                            })
                    }
                }
            }
        }
    }
}