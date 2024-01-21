package com.markusw.cosasdeunicorapp.auth.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.utils.Result
import javax.inject.Inject

class RegisterWithNameEmailAndPassword @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        return authRepository.register(name, email, password)
    }

}