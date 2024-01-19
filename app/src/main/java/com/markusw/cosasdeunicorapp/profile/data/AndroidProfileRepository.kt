package com.markusw.cosasdeunicorapp.profile.data

import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.use_cases.GetLoggedUser
import com.markusw.cosasdeunicorapp.profile.domain.repository.ProfileRepository

class AndroidProfileRepository(
    private val authService: AuthService,
    private val getLoggedUserUseCase: GetLoggedUser,
): ProfileRepository {
    override suspend fun updateProfile(data: ProfileUpdateData): Result<Unit> {
        return authService.updateUserProfileData(data)
    }

    override suspend fun sendPasswordResetByEmail(email: String): Result<Unit> {
        return authService.sendPasswordResetByEmail(email)
    }

    override fun getLoggedUser(): User {
        return getLoggedUserUseCase()
    }
}