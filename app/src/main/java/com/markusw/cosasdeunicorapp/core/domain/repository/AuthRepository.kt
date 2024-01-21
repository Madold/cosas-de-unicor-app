package com.markusw.cosasdeunicorapp.core.domain.repository

import com.markusw.cosasdeunicorapp.core.utils.Result

interface AuthRepository {

    suspend fun sendPasswordResetByEmail(email: String): Result<Unit>

}