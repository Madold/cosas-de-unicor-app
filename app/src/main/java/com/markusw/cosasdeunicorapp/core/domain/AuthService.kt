package com.markusw.cosasdeunicorapp.core.domain

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.core.utils.Result

interface AuthService {
    suspend fun authenticate(email: String, password: String): Result<Unit>
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit>
    suspend fun sendPasswordResetByEmail(email: String): Result<Unit>
}