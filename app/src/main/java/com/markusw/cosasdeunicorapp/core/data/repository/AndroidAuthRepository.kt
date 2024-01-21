package com.markusw.cosasdeunicorapp.core.data.repository

import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result

class AndroidAuthRepository(
    private val authService: AuthService,
): AuthRepository {
    override suspend fun sendPasswordResetByEmail(email: String): Result<Unit> {
        return try {
            authService.sendPasswordResetByEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

}