package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * Created by Markus on 4-12-2023.
 * Use case that observes new messages
 * @param chatRepository the repository to observe the messages from
 * @see ChatRepository
 */
class ObserveNewMessages @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke() = chatRepository.onNewMessage()

}