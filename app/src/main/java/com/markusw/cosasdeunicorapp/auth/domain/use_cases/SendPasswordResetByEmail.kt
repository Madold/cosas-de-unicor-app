package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.utils.Resource
import javax.inject.Inject

class SendPasswordResetByEmail @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(email: String): Resource<Unit> {
        return authService.sendPasswordResetByEmail(email)
    }
}