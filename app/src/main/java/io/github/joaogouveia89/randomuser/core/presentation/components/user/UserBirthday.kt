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
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

private val timeFormat = LocalDateTime.Format {
    dayOfMonth()
    char('/')
    monthNumber()
    char('/')
    year()
}

@Composable
fun UserBirthday(
    date: Instant,
    age: Int,
    iconBackgroundColor: Color,
    iconColor: Color
) {
    val localTime = date.toLocalDateTime(TimeZone.UTC)

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
            imageVector = Icons.Default.Cake,
            contentDescription = null,
            tint = iconColor,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = timeFormat.format(localTime),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$age years old",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserBirthdayPreview() {
    // Example dynamic colors (replace these with your analyzed colors)
    val dynamicIconColor = Color(0xFF6200EE)      // Example purple color
    val dynamicIconBackgroundColor = Color(0xFF00FFFF) // Light purple background

    UserBirthday(
        date = Clock.System.now(),
        age = 34,
        iconBackgroundColor = dynamicIconBackgroundColor,
        iconColor = dynamicIconColor
    )
}