package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.utils.Result
import javax.inject.Inject

class LoginWithCredential @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(credential: AuthCredential): Result<Unit> {
        return authService.authenticateWithCredential(credential)
    }
}