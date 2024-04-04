package com.markusw.cosasdeunicorapp.core.data.repository

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.utils.Result

class FakeAuthRepository: AuthRepository {
    override suspend fun authenticate(email: String, password: String): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun sendPasswordResetByEmail(email: String): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun updateUserProfileData(data: ProfileUpdateData): Result<Unit> {
        return Result.Success(Unit)
    }

    override suspend fun getLoggedUser(): Result<User> {
        return Result.Success(User())
    }
}