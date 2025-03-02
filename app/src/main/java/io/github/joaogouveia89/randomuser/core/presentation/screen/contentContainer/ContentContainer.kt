package io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.joaogouveia89.randomuser.R
import io.github.joaogouveia89.randomuser.core.presentation.components.ErrorSnackBar
import io.github.joaogouveia89.randomuser.core.presentation.screen.GenericErrorScreen
import io.github.joaogouveia89.randomuser.core.presentation.screen.OfflineScreen
import io.github.joaogouveia89.randomuser.core.presentation.screen.contentContainer.state.ContentState

@Composable
fun ContentContainer(
    contentState: ContentState,
    content: @Composable () -> Unit,
    showSnackBar: Boolean = false,
    onErrorRetry: () -> Unit,
    onSnackBarDismiss: (() -> Unit)?
) {
    when (contentState) {
        ContentState.Ready -> Content(content)
        ContentState.Loading -> Loading()
        ContentState.Offline -> if (showSnackBar)
            ContentWithErrorSnackBar(
                content = content,
                snackBarErrorMessage = stringResource(R.string.error_message_offline),
                onCloseErrorBarClick = null,
                snackBarLeadIcon = Icons.Default.WifiOff
            ) else OfflineScreen()

        is ContentState.Error -> if(showSnackBar){
            ContentWithErrorSnackBar(
                content = content,
                snackBarErrorMessage = stringResource(R.string.error_message_chronometer),
                onCloseErrorBarClick = onSnackBarDismiss
            )
        } else GenericErrorScreen(onRetry = onErrorRetry)
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Content(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        content()
    }
}

@Composable
private fun ContentWithErrorSnackBar(
    content: @Composable () -> Unit,
    snackBarErrorMessage: String,
    snackBarLeadIcon: ImageVector? = null,
    onCloseErrorBarClick: (() -> Unit)?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        content()
        ErrorSnackBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            icon = snackBarLeadIcon,
            messageRes = snackBarErrorMessage,
            onCloseErrorBarClick = onCloseErrorBarClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentContainerLoadingPreview() {
    ContentContainer(
        contentState = ContentState.Loading,
        onErrorRetry = {},
        onSnackBarDismiss = {},
        content = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ContentContainerOfflinePreview() {
    ContentContainer(
        contentState = ContentState.Offline,
        onErrorRetry = {},
        onSnackBarDismiss = {},
        content = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ContentContainerErrorPreview() {
    ContentContainer(
        contentState = ContentState.Error(0),
        onErrorRetry = {},
        onSnackBarDismiss = {},
        content = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun ContentContainerReadyPreview() {
    ContentContainer(
        contentState = ContentState.Ready,
        onErrorRetry = {},
        onSnackBarDismiss = {},
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Content ready"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ContentContainerErrorWithSnackBarPreview() {
    ContentContainer(
        contentState = ContentState.Error(0),
        showSnackBar = true,
        onErrorRetry = {},
        onSnackBarDismiss = {},
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Content ready"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ContentContainerOfflineWithSnackBarPreview() {
    ContentContainer(
        contentState = ContentState.Offline,
        showSnackBar = true,
        onErrorRetry = {},
        onSnackBarDismiss = {},
        content = {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Content ready"
                )
            }
        }
    )
}