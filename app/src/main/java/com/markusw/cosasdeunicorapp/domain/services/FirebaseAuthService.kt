package com.markusw.cosasdeunicorapp.domain.services

import com.google.firebase.auth.FirebaseAuth
import com.markusw.cosasdeunicorapp.core.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    private val auth: FirebaseAuth
): AuthService {
    override suspend fun authenticate(email: String, password: String): Resource<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }

}