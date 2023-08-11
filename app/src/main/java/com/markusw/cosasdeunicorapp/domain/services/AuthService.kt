package com.markusw.cosasdeunicorapp.domain.services

import com.markusw.cosasdeunicorapp.core.utils.Resource

interface AuthService {
    suspend fun authenticate(email: String, password: String): Resource<Unit>
    suspend fun register(email: String, password: String): Resource<Unit>
    suspend fun logout(): Resource<Unit>
}