package io.github.joaogouveia89.randomuser.core.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.joaogouveia89.randomuser.R

@Composable
fun OfflineScreen(
    modifier: Modifier = Modifier
) {
    GenericScreen(
        modifier = modifier,
        icon = Icons.Default.WifiOff,
        description = stringResource(R.string.error_offline_screen_description),
    )
}

@Preview(showBackground = true)
@Composable
private fun OfflineScreenPreview() {
    OfflineScreen()
}