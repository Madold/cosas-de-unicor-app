package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import javax.inject.Inject

class LoadPreviousMessages @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke() = chatRepository.loadPreviousMessages()

}