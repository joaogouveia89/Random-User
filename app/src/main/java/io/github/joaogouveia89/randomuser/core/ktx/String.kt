package io.github.joaogouveia89.randomuser.core.ktx

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

fun String.asClickableUrl() = buildAnnotatedString {
    val url = this@asClickableUrl
    append(url)
    addStyle(
        style = SpanStyle(
            color = Color(0xff0000EE),
            textDecoration = TextDecoration.Underline
        ), start = 0, end = url.length
    )
}