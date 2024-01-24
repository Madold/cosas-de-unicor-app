package com.markusw.cosasdeunicorapp.core.presentation

/**
 * This interface is used to provide the text for the permission request dialog.
 */
interface PermissionTextProvider {
    /**
     * Returns the text for the permission request dialog.
     * @param isPermanentlyDeclined true if the user has permanently declined the permission.
     * @return the text for the permission request dialog.
     * @see UiText
     */
    fun getDescription(isPermanentlyDeclined: Boolean): UiText

    /**
     * Returns the illustration resource for the permission request dialog.
     * @return the title for the permission request dialog.
     * @see UiText
     */
    fun getIllustration(): Int
}