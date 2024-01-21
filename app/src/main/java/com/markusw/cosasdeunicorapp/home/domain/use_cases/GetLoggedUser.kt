package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.model.User
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetLoggedUser @Inject constructor(
    private val profileRepository: ProfileRepository
) {
   suspend operator fun invoke(): Result<User> = profileRepository.getLoggedUser()
}