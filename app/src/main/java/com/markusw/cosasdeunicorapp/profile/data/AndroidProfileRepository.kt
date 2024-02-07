package com.markusw.cosasdeunicorapp.profile.data

import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class AndroidProfileRepository(
    private val authService: AuthService,
    private val remoteDatabase: RemoteDatabase
) : ProfileRepository {
    override suspend fun updateProfile(data: ProfileUpdateData): Result<Unit> {
        return try {
            authService.updateUserProfileData(data)
            Result.Success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            Timber.e(e)
            Result.Error(UiText.DynamicString("El correo ya está en uso"))
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            Timber.e(e)
            Result.Error(UiText.DynamicString("Lo sentimos, pero esta acción requiere que vuelvas a iniciar sesión para garantizar la seguridad de tu cuenta. Por favor, vuelve a iniciar sesión para continuar."))
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

    override suspend fun sendPasswordResetByEmail(email: String): Result<Unit> {
        return try {
            authService.sendPasswordResetByEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

    override suspend fun onUserInfoUpdate(): Result<Flow<User>> {
        return try {
            Result.Success(remoteDatabase.onUserInfoUpdate())
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }


}