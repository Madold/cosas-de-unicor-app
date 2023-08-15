package com.markusw.cosasdeunicorapp.domain.services

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.core.utils.Resource

interface AuthService {
    suspend fun authenticate(email: String, password: String): Resource<Unit>
    suspend fun register(email: String, password: String): Resource<Unit>
    suspend fun logout(): Resource<Unit>
    suspend fun authenticateWithCredential(credential: AuthCredential): Resource<Unit>
    suspend fun sendPasswordResetByEmail(email: String): Resource<Unit>
}