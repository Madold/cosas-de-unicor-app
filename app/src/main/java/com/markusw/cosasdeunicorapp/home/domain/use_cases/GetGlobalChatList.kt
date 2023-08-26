package com.markusw.cosasdeunicorapp.home.domain.use_cases

import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGlobalChatList @Inject constructor(
    private val chatRepository: ChatRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<Message>>> {
        return chatRepository.getGlobalChatList()
    }

}