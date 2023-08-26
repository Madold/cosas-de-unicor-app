package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.utils.Resource
import javax.inject.Inject

class LoginWithEmailAndPassword @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(email: String, password: String): Resource<Unit> {
        return authService.authenticate(email, password)
    }
}