package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.AuthService
import com.markusw.cosasdeunicorapp.core.utils.Resource
import javax.inject.Inject

class Logout @Inject constructor(
    private val authService: AuthService
) {
    suspend operator fun invoke(): Resource<Unit> {
        return authService.logout()
    }

}