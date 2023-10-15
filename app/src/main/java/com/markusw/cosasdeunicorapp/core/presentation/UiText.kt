package com.markusw.cosasdeunicorapp.core.presentation

import android.content.Context
import android.support.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * This class represents a text that can be either a string resource or a dynamic string.
 * @see StringResource
 * @see DynamicString
 * @see asString
 */
sealed class UiText {
    /**
     * This class represents a dynamic string from an API or other source.
     * @param value the string value.
     * @see asString
     */
    data class DynamicString(val value: String) : UiText()

    /**
     * This class represents a string resource.
     * @param resId the resource id of the string.
     * @param args the arguments to be substituted in the string.
     * @see asString
     */
    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any
    ): UiText()

    /**
     * Returns the string value of this UiText in a context of a composable function.
     * @see stringResource
     * @see Context.getString
     * @see asString
     * @see asString
     * @see asString
     * @see asString
     * @return the string value of this UiText.
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    /**
     * Returns the string value of this UiText in a context of an activity or fragment.
     * @param context the context.
     * @see Context.getString
     * @see asString
     * @return the string value of this UiText.
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}
