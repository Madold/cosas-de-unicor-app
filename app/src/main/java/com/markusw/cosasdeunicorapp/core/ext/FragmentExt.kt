package com.markusw.cosasdeunicorapp.core.ext

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markusw.cosasdeunicorapp.core.utils.UiText
import com.markusw.cosasdeunicorapp.R

fun Fragment.toast(message: UiText, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.context, message.asString(this.requireContext()), duration).show()
}


fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.context, message, duration).show()
}

fun Fragment.showDialog(
    message: UiText,
    title: UiText = UiText.StringResource(R.string.error),
    positiveButtonText: UiText? = null,
    onPositiveButtonClick: (() -> Unit)? = null,
    negativeButtonText: UiText? = null,
    negativeButtonOnClick: (() -> Unit)? = null,
    neutralButtonText: UiText? = null,
    neutralButtonOnClick: (() -> Unit)? = null
) {
    this.context?.let {
        MaterialAlertDialogBuilder(it).apply {
            setTitle(title.asString(it))
            setMessage(message.asString(it))
            positiveButtonText?.let { positiveText ->
                setPositiveButton(positiveText.asString(it)) { dialog, _ ->
                    onPositiveButtonClick?.invoke()
                    dialog.dismiss()
                }
            }
            negativeButtonText?.let { negativeText ->
                setNegativeButton(negativeText.asString(it)) { dialog, _ ->
                    negativeButtonOnClick?.invoke()
                    dialog.dismiss()
                }
            }
            neutralButtonText?.let { neutralText ->
                setNeutralButton(neutralText.asString(it)) { dialog, _ ->
                    neutralButtonOnClick?.invoke()
                    dialog.dismiss()
                }
            }
        }.show()
    }
}