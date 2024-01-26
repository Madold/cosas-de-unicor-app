package com.markusw.cosasdeunicorapp.profile.domain.repository

import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.utils.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun updateProfile(data: ProfileUpdateData): Result<Unit>

    suspend fun sendPasswordResetByEmail(email: String): Result<Unit>
    suspend fun onUserInfoUpdate(): Result<Flow<User>>
}

