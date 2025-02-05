package io.github.joaogouveia89.randomuser.core.presentation.components

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun ErrorSnackBar(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    @StringRes messageRes: Int?,
    onCloseErrorBarClick: (() -> Unit)?
) {
    messageRes?.let {
        val elementsColor = Color.White
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        tint = elementsColor,
                        contentDescription = null
                    )
                }
                Text(
                    color = elementsColor,
                    text = stringResource(it)
                )
            }


            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
                    .clickable { onCloseErrorBarClick?.invoke() },
                imageVector = Icons.Default.Close,
                tint = elementsColor,
                contentDescription = null
            )
        }
    }
}