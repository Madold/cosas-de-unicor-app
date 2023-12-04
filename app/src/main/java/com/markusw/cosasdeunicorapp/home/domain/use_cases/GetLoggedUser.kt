package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.google.firebase.auth.FirebaseAuth
import com.markusw.cosasdeunicorapp.core.ext.toDomainModel
import com.markusw.cosasdeunicorapp.core.domain.model.User
import javax.inject.Inject

class GetLoggedUser @Inject constructor(
    private val auth: FirebaseAuth
) {
   operator fun invoke(): User = auth.currentUser!!.toDomainModel()
}