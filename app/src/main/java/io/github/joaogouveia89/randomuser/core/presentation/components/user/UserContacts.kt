package io.github.joaogouveia89.randomuser.core.presentation.components.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.joaogouveia89.randomuser.core.ktx.asClickableUrl

@Composable
fun UserContacts(
    phone: String,
    cellPhone: String,
    email: String,
    iconBackgroundColor: Color,
    iconColor: Color,
    onCopyEmailToClipboard: () -> Unit,
    onDialRequired: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconBackgroundColor, shape = RoundedCornerShape(10.dp)),
                imageVector = Icons.Default.Phone,
                contentDescription = null,
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    modifier = Modifier
                        .clickable { onDialRequired(phone) },
                    text = phone.asClickableUrl(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier
                        .clickable { onDialRequired(cellPhone) },
                    text = cellPhone.asClickableUrl(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .background(iconBackgroundColor, shape = RoundedCornerShape(10.dp)),
                imageVector = Icons.Default.Mail,
                contentDescription = null,
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                modifier = Modifier
                    .clickable { onCopyEmailToClipboard() }
                    .padding(start = 12.dp),
                imageVector = Icons.Default.ContentCopy,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserContactsPreview() {
    // Example dynamic colors (replace these with your analyzed colors)
    val dynamicIconColor = Color(0xFF6200EE)      // Example purple color
    val dynamicIconBackgroundColor = Color(0xFF00FFFF) // Light purple background
    UserContacts(
        phone = "2222 222 2222",
        cellPhone = "3333 3333 333",
        email = "johndoe@gmail.com",
        iconBackgroundColor = dynamicIconBackgroundColor,
        iconColor = dynamicIconColor,
        onCopyEmailToClipboard = {},
        onDialRequired = {}
    )
}