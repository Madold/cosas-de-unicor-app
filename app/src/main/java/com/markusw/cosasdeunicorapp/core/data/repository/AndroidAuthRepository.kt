package com.markusw.cosasdeunicorapp.core.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.EmailNotVerifiedException
import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.domain.ProfileUpdateData
import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.domain.repository.AuthRepository
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result
import timber.log.Timber

class AndroidAuthRepository(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun authenticate(email: String, password: String): Result<Unit> {
        return try {
            authService.authenticate(email, password)
            Result.Success(Unit)
        } catch (e: EmailNotVerifiedException) {
            Result.Error(UiText.StringResource(R.string.account_not_verified))
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            authService.register(name, email, password)
            Timber.d("Successfully registered user")
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e)
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authService.logout()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

    override suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit> {
        return try {
            authService.authenticateWithCredential(credential)
            Result.Success(Unit)
        } catch(e: FirebaseNetworkException) {
            Result.Error(UiText.DynamicString("Error de conexión. Verifica tu conexión a internet e intenta de nuevo."))
        } catch (e: Exception) {
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

    override suspend fun updateUserProfileData(data: ProfileUpdateData): Result<Unit> {
        return try {
            authService.updateUserProfileData(data)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

    override suspend fun getLoggedUser(): Result<User> {
        return try {
            Result.Success(authService.getLoggedUser())
        } catch (e: Exception) {
            Result.Error(UiText.DynamicString(e.toString()))
        }
    }

}