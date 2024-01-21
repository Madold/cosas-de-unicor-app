package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.utils.Result
import javax.inject.Inject

class LoginWithCredential @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credential: AuthCredential): Result<Unit> {
        return authRepository.authenticateWithCredential(credential)
    }
}