package com.markusw.cosasdeunicorapp.home.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.ext.prepend
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.core.utils.TimeUtils
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.MessageContent
import com.markusw.cosasdeunicorapp.home.domain.use_cases.DownloadDocument
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val observeNewMessages: ObserveNewMessages,
    private val loadPreviousMessages: LoadPreviousMessages,
    private val sendMessageToGlobalChat: SendMessageToGlobalChat,
    private val getLoggedUser: GetLoggedUser,
    private val logout: Logout,
    private val downloadDocument: DownloadDocument
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()
    private val homeEventsChannel = Channel<HomeEvents>()
    val homeEvents = homeEventsChannel.receiveAsFlow()
    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    init {
        fetchPreviousGlobalMessages()

        viewModelScope.launch(dispatchers.io) {
            observeNewMessages().collectLatest { newMessage ->
                _uiState.update { it.copy(globalChatList = it.globalChatList.prepend(newMessage)) }
            }
        }

        _uiState.update { it.copy(currentUser = getLoggedUser()) }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.FetchPreviousGlobalMessages -> {
                fetchPreviousGlobalMessages()
            }

            is HomeUiEvent.CloseSession -> {
                viewModelScope.launch(dispatchers.io) {
                    val authResult = logout()
                    handleLogoutResult(authResult)
                }
            }

            is HomeUiEvent.MessageChanged -> {
                _uiState.update { it.copy(message = event.message) }
            }

            is HomeUiEvent.SendMessageToGlobalChat -> {
                val message = uiState.value.message.trim()
                val sender = uiState.value.currentUser

                resetMessageField()
                viewModelScope.launch(dispatchers.io) {
                    sendMessageToGlobalChat(
                        Message(
                            content = MessageContent(text = message, replyTo = uiState.value.repliedMessage),
                            sender = sender,
                            timestamp = TimeUtils.getDeviceHourInTimestamp()
                        )
                    )
                }
            }

            is HomeUiEvent.ClearReplyMessage -> {
                _uiState.update { it.copy(repliedMessage = null) }
            }

            is HomeUiEvent.ReplyToMessage -> {
                _uiState.update { it.copy(repliedMessage = event.message) }
            }

            is HomeUiEvent.DownloadDocument -> {
                viewModelScope.launch {
                    when (val downloadResult = downloadDocument(event.fileName)) {
                        is Resource.Error -> {

                        }

                        is Resource.Success -> {
                            Timber.d("Downloaded file Uri: ${downloadResult.data}")
                        }
                    }

                }
            }
        }
    }

    private fun fetchPreviousGlobalMessages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingPreviousGlobalMessages = true) }
            loadPreviousMessages().also { previousMessages ->
                _uiState.update {
                    it.copy(
                        globalChatList = it.globalChatList + previousMessages
                    )
                }
            }
            _uiState.update { it.copy(isFetchingPreviousGlobalMessages = false) }
        }
    }

    private suspend fun handleLogoutResult(result: Resource<Unit>) {
        when (result) {
            is Resource.Error -> {

            }

            is Resource.Success -> {
                homeEventsChannel.send(HomeEvents.LogoutSuccessful)
            }
        }
    }

    private fun resetMessageField() {
        _uiState.update { it.copy(message = "") }
    }

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    override fun onCleared() {
        super.onCleared()
        homeEventsChannel.close()
    }

}