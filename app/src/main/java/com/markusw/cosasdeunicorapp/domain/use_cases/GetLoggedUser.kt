package com.markusw.cosasdeunicorapp.domain.use_cases

import com.google.firebase.auth.FirebaseAuth
import com.markusw.cosasdeunicorapp.core.ext.toUserModel
import com.markusw.cosasdeunicorapp.data.model.User
import javax.inject.Inject

class GetLoggedUser @Inject constructor(
    private val auth: FirebaseAuth
) {
   operator fun invoke(): User = auth.currentUser!!.toUserModel()
}