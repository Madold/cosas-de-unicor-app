package com.markusw.cosasdeunicorapp.core.data

import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.markusw.cosasdeunicorapp.R
import com.markusw.cosasdeunicorapp.core.EmailNotVerifiedException
import com.markusw.cosasdeunicorapp.core.presentation.UiText
import com.markusw.cosasdeunicorapp.core.utils.Result

/**
 * Created by Markus on 3-12-2023.
 * Represents an abstraction of a Firebase service.
 */
abstract class FirebaseService {

    /**
     * Executes a Firebase operation and returns a [Result] with the result of the operation.
     * If the operation fails, the [Result] will contain an [UiText.StringResource] with the
     * error message.
     * @param operation The operation to execute.
     * @return A [Result] with the result of the operation.
     * @see Result
     * @see UiText.StringResource
     */
    protected suspend fun <T> executeFirebaseOperation(operation: suspend () -> T): Result<T> {
        return try {
            Result.Success(operation())
        } catch (e: EmailNotVerifiedException) {
            Result.Error(UiText.StringResource(R.string.account_not_verified))
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.Error(UiText.StringResource(R.string.user_not_exist))
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.Error(UiText.StringResource(R.string.user_already_registered))
        } catch (e: Exception) {
            Result.Error(
                UiText.StringResource(
                    R.string.unknownException,
                    "${e.javaClass}: ${e.message}"
                )
            )
        }
    }

}