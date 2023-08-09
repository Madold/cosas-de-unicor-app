package com.markusw.cosasdeunicorapp.core.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.context, message, duration).show()
}

fun Fragment.showDialog(
    message: String,
    title: String = "Error",
    positiveButtonText: String? = null,
    onPositiveButtonClick: (() -> Unit)? = null,
    negativeButtonText: String? = null,
    negativeButtonOnClick: (() -> Unit)? = null,
    neutralButtonText: String? = null,
    neutralButtonOnClick: (() -> Unit)? = null
) {
    this.context?.let {
        MaterialAlertDialogBuilder(it).apply {
            setTitle(title)
            setMessage(message)
            positiveButtonText?.let { positiveText ->
                setPositiveButton(positiveText) { dialog, _ ->
                    onPositiveButtonClick?.invoke()
                    dialog.dismiss()
                }
            }
            negativeButtonText?.let { negativeText ->
                setNegativeButton(negativeText) { dialog, _ ->
                    negativeButtonOnClick?.invoke()
                    dialog.dismiss()
                }
            }
            neutralButtonText?.let { neutralText ->
                setNeutralButton(neutralText) { dialog, _ ->
                    neutralButtonOnClick?.invoke()
                    dialog.dismiss()
                }
            }
        }.show()
    }
}