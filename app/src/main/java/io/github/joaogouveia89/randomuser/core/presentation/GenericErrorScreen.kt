package io.github.joaogouveia89.randomuser.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.joaogouveia89.randomuser.R

@Composable
fun GenericErrorScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    GenericScreen(
        modifier = modifier,
        icon = Icons.Default.SentimentVeryDissatisfied,
        description = stringResource(R.string.error_generic_screen_description),
        action = {
            Button(
                onClick = onRetry,
                content = {
                    Text(text = stringResource(R.string.retry))
                }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun GenericErrorScreenPreview() {
    GenericErrorScreen(
        onRetry = {}
    )
}