package com.markusw.cosasdeunicorapp.domain.services

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.markusw.cosasdeunicorapp.core.utils.Resource
import kotlinx.coroutines.tasks.await

class FirebaseAuthService constructor(
    private val auth: FirebaseAuth
) : AuthService {
    override suspend fun authenticate(email: String, password: String): Resource<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()

            auth.currentUser?.let {
                if (!it.isEmailVerified) {
                    it.sendEmailVerification().await()
                   return Resource.Error("La cuenta no está verificada. Se ha enviado un correo de verificación.")
                }
            }

            Resource.Success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Resource.Error("No existe un usuario con ese correo.")
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun register(email: String, password: String): Resource<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.signOut()
            Resource.Success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            Resource.Error("Ya hay un usuario registrado con ese correo.")
        } catch (e: Exception) {
            Resource.Error("${e.javaClass}: ${e.message.toString()}")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            auth.signOut()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("${e.javaClass}: ${e.message}")
        }
    }

    override suspend fun authenticateWithCredential(credential: AuthCredential): Resource<Unit> {
        return try {
            auth.signInWithCredential(credential).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("${e.javaClass}: ${e.message}")
        }
    }

}