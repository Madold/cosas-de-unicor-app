package com.markusw.cosasdeunicorapp.core.domain

import com.google.firebase.auth.AuthCredential
import com.markusw.cosasdeunicorapp.core.utils.Result

/**
 * Created by Markus Water on 3-12-2023.
 * This interface defines the methods that the AuthService must implement
 */
interface AuthService {
    /**
     * Authenticates a user with the given email and password
     * @param email the email of the user
     * @param password the password of the user
     * @return a Result object
     * @see Result
     */
    suspend fun authenticate(email: String, password: String): Result<Unit>
    /**
     * Registers a user with the given name, email and password
     * @param name the name of the user
     * @param email the email of the user
     * @param password the password of the user
     * @return a Result object
     * @see Result
     */
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    /**
     * Logs out the user
     * @return a Result object
     * @see Result
     */
    suspend fun logout(): Result<Unit>
    /**
     * Authenticates the user with the given credential
     * @param credential the credential to authenticate with
     * @return a Result object
     * @see Result
     */
    suspend fun authenticateWithCredential(credential: AuthCredential): Result<Unit>
    /**
     * Sends a password reset email to the given email
     * @param email the email to send the password reset email to
     * @return a Result object
     * @see Result
     */
    suspend fun sendPasswordResetByEmail(email: String): Result<Unit>

    /**
     * Updates the user profile data
     * @param data the data to update the user profile with
     * @return a Result object
     * @see Result
     * @see ProfileUpdateData
     */
    suspend fun updateUserProfileData(data: ProfileUpdateData): Result<Unit>

}