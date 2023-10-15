package com.markusw.cosasdeunicorapp.core.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

/**
 * Shows a toast message.
 * @param message The message to show.
 * @param duration The duration of the toast.
 * @see Toast
 * @see Toast.LENGTH_SHORT
 * @see Toast.LENGTH_LONG
 * @see Toast.makeText
 * @see Toast.show
 * @see Context
 */
fun Context.toast(message: String, duration: Int= Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Opens the app settings screen.
 */
fun Context.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", this.packageName, this::class.java.simpleName)
    ).also(::startActivity)
}