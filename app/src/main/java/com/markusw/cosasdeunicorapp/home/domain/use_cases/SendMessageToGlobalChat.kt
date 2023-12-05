package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import timber.log.Timber
import javax.inject.Inject

class SendMessageToGlobalChat @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(message: Message): Result<Unit> {
        Timber.d("Sending message with reply: ${message.content.replyTo?.content?.text}")
        return chatRepository.sendMessageToGlobalChat(message)
    }

}