package com.markusw.cosasdeunicorapp.core.presentation

import com.markusw.cosasdeunicorapp.R

class ReadExternalStoragePermission: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): UiText {
        if (isPermanentlyDeclined) {
            return UiText.StringResource(R.string.read_external_storage_permission_permanently_denied)
        }

        return UiText.StringResource(R.string.read_external_storage_permission_rationale)

    }

}