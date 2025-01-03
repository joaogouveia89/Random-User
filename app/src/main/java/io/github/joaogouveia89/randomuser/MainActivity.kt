package io.github.joaogouveia89.randomuser

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.joaogouveia89.randomuser.randomUser.presentation.RandomUserScreen
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserCommand
import io.github.joaogouveia89.randomuser.randomUser.presentation.viewModel.RandomUserViewModel
import io.github.joaogouveia89.randomuser.ui.theme.RandomUserTheme

private const val COPY_EMAIL_TO_CLIPBOARD_LABEL = "random_user_email"

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: RandomUserViewModel by viewModels()

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
                            onAddToContactsClick = { viewModel.execute(RandomUserCommand.SaveUser) },
                            onOpenMapClick = {
                                val gmmIntentUri: Uri =
                                    Uri.parse(uiState.user.getMapsIntentQuery())
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                startActivity(mapIntent)
                            },
                            onCopyEmailToClipboard = {
                                val clipboard: ClipboardManager = getSystemService(
                                    CLIPBOARD_SERVICE
                                ) as ClipboardManager

                                val clip = ClipData.newPlainText(COPY_EMAIL_TO_CLIPBOARD_LABEL, uiState.user.email)
                                clipboard.setPrimaryClip(clip)
                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
                                    Toast.makeText(baseContext, getString(R.string.email_copied_to_clipboard), Toast.LENGTH_SHORT).show()
                            },
                            onDialRequired = { phoneNumber ->
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$phoneNumber")
                                }
                                if (intent.resolveActivity(packageManager) != null) {
                                    startActivity(intent)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}