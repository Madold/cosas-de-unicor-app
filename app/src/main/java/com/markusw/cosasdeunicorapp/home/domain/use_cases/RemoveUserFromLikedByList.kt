package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.domain.model.User
import javax.inject.Inject

class RemoveUserFromLikedByList @Inject constructor(
    private val remoteDatabase: RemoteDatabase
) {

    suspend operator fun invoke(newsId: String, userId: String) {
        remoteDatabase.removeUserFromLikedByList(newsId, userId)
    }

}