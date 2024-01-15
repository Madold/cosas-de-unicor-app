package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import javax.inject.Inject

class GetUsersCount @Inject constructor(
    private val remoteDatabase: RemoteDatabase
) {

    suspend operator fun invoke() = remoteDatabase.getUsersCount()

}