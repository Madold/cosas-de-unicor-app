package com.markusw.cosasdeunicorapp.core.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.context, message, duration).show()
}

fun Fragment.simpleDialog(message: String, title: String = "Error") {
    this.context?.let {
        MaterialAlertDialogBuilder(it).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Ok") {
                dialog, _
                -> dialog.dismiss()
            }
        }.show()
    }
}