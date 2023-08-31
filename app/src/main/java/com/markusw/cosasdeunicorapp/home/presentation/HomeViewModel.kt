package com.markusw.cosasdeunicorapp.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.ext.prepend
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.core.utils.TimeUtils
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.use_cases.GetLoggedUser
import com.markusw.cosasdeunicorapp.home.domain.use_cases.LoadPreviousMessages
import com.markusw.cosasdeunicorapp.home.domain.use_cases.Logout
import com.markusw.cosasdeunicorapp.home.domain.use_cases.ObserveNewMessages
import com.markusw.cosasdeunicorapp.home.domain.use_cases.SendMessageToGlobalChat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val observeNewMessages: ObserveNewMessages,
    private val loadPreviousMessages: LoadPreviousMessages,
    private val sendMessageToGlobalChat: SendMessageToGlobalChat,
    private val getLoggedUser: GetLoggedUser,
    private val logout: Logout
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()
    private val homeEventsChannel = Channel<HomeEvents>()
    val homeEvents = homeEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingPreviousGlobalMessages = true) }
            loadPreviousMessages().also { previousMessages ->
                _uiState.update {
                    it.copy(
                        globalChatList = it.globalChatList + previousMessages,
                        isFetchingPreviousGlobalMessages = false
                    )
                }
            }
        }


        viewModelScope.launch(dispatchers.io) {
            observeNewMessages().collectLatest { newMessage ->
                _uiState.update { it.copy(globalChatList = it.globalChatList.prepend(newMessage)) }
            }
        }


        _uiState.update { it.copy(currentUser = getLoggedUser()) }
    }

    fun onCloseSession() {
        viewModelScope.launch(dispatchers.io) {
            when (val authResult = logout()) {
                is Resource.Error -> {

                }

                is Resource.Success -> {
                    homeEventsChannel.send(HomeEvents.LogoutSuccessful)
                }
            }
        }
    }

    fun onMessageChange(message: String) {
        _uiState.update { it.copy(message = message) }
    }

    fun onMessageSent() {
        val message = uiState.value.message.trim()
        val sender = uiState.value.currentUser
        resetMessageField()
        viewModelScope.launch(dispatchers.io) {
            sendMessageToGlobalChat(
                Message(
                    message,
                    sender,
                    TimeUtils.getDeviceHourInTimestamp()
                )
            )
        }
    }

    fun onTopOfListReached() {
        _uiState.update { it.copy(isFetchingPreviousGlobalMessages = true) }
        viewModelScope.launch(dispatchers.io) {
            loadPreviousMessages().also { previousMessages ->
                _uiState.update {
                    it.copy(
                        globalChatList = it.globalChatList + previousMessages,
                        isFetchingPreviousGlobalMessages = false
                    )
                }
            }
        }
    }

    private fun resetMessageField() {
        _uiState.update { it.copy(message = "") }
    }

    override fun onCleared() {
        super.onCleared()
        homeEventsChannel.close()
    }

}