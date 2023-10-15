package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.home.domain.repository.RemoteStorage
import javax.inject.Inject

class DownloadDocument @Inject constructor(
    private val remoteStorage: RemoteStorage
) {

    suspend operator fun invoke(fileName: String) = remoteStorage.downloadDocument(fileName)

}