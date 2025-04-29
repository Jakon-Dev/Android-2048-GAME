package com.jakondev.a2048_game.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun sendEmail(context: Context, subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Only email apps should handle this
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    // Start the email app if available
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
