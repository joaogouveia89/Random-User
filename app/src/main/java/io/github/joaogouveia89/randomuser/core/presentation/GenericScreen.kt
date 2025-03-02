package io.github.joaogouveia89.randomuser.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joaogouveia89.randomuser.R

@Composable
fun GenericScreen(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    description: String,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            text = description,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        action?.invoke()
    }
}

@Preview(showBackground = true)
@Composable
private fun GenericScreenNoActionPreview() {
    GenericScreen(
        icon = Icons.Default.WifiOff,
        description = stringResource(R.string.error_offline_screen_description),
    )
}

@Preview(showBackground = true)
@Composable
private fun GenericScreenWithActionPreview() {
    GenericScreen(
        icon = Icons.Default.WifiOff,
        description = stringResource(R.string.error_offline_screen_description),
        action = {
            Button(onClick = {}) {
                Text("Retry")
            }
        }
    )
}