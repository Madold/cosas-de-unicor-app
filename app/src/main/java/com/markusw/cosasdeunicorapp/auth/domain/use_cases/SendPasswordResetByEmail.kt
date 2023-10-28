package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.utils.Result
import javax.inject.Inject

class SendPasswordResetByEmail @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        return authService.sendPasswordResetByEmail(email)
    }
}