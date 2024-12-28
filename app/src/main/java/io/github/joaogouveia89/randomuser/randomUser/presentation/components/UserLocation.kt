package io.github.joaogouveia89.randomuser.randomUser.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserLocation(
    city: String,
    state: String,
    country: String,
    iconBackgroundColor: Color,
    iconColor: Color,
    onOpenMapClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .size(36.dp)
                .background(iconBackgroundColor, shape = RoundedCornerShape(10.dp)),
            imageVector = Icons.Default.Place,
            contentDescription = null,
            tint = iconColor
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$city, $state",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = country,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(
            onClick = onOpenMapClick
        ) {
            Icon(
                imageVector = Icons.Default.Map,
                contentDescription = "Open Map",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserLocationPreview() {
    UserLocation(
        city = "Lisbon",
        state = "Lisbon",
        country = "Portugal",
        iconColor = Color.Red,
        iconBackgroundColor = Color.Yellow
    ) { }
}