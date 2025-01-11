package io.github.joaogouveia89.randomuser.randomUser

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import io.github.joaogouveia89.randomuser.R

fun Context.openMaps(query: String) {
    val gmmIntentUri: Uri =
        Uri.parse(query)
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    startActivity(mapIntent)
}

fun Context.copyEmailToClipboard(email: String) {
    val clipboard: ClipboardManager = getSystemService(
        CLIPBOARD_SERVICE
    ) as ClipboardManager

    val clip = ClipData.newPlainText(COPY_EMAIL_TO_CLIPBOARD_LABEL, email)
    clipboard.setPrimaryClip(clip)
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
        Toast.makeText(this, getString(R.string.email_copied_to_clipboard), Toast.LENGTH_SHORT)
            .show()
}

fun Context.dial(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}