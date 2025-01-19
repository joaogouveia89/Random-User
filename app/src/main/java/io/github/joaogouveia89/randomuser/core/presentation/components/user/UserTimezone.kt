package io.github.joaogouveia89.randomuser.core.presentation.components.user

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
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserTimezone(
    timezone: String,
    timezoneDescription: String,
    localTime: String,
    iconBackgroundColor: Color,
    iconColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Apply background color if provided
        val iconModifier = Modifier
            .size(36.dp) // Slightly larger area for background effect
            .let { base ->
                iconBackgroundColor.let { color ->
                    base.background(
                        color,
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
            .padding(6.dp) // Inner padding for the icon itself

        Icon(
            imageVector = Icons.Default.Timer,
            contentDescription = null,
            tint = iconColor, // Dynamic icon tint
            modifier = iconModifier
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = timezone,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = timezoneDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = localTime,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserTimezonePreview() {
    // Example dynamic colors (replace these with your analyzed colors)
    val dynamicIconColor = Color(0xFF6200EE)      // Example purple color
    val dynamicIconBackgroundColor = Color(0xFF00FFFF) // Light purple background
    UserTimezone(
        timezone = "0:00",
        timezoneDescription = "Lisbon",
        localTime = "15:20",
        iconBackgroundColor = dynamicIconBackgroundColor,
        iconColor = dynamicIconColor
    )
}