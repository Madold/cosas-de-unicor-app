package com.markusw.cosasdeunicorapp.home.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.ext.prepend
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.core.utils.TimeUtils
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.MessageContent
import com.markusw.cosasdeunicorapp.home.domain.use_cases.DownloadDocument
import com.markusw.cosasdeunicorapp.home.domain.use_cases.GetLoggedUser
import com.markusw.cosasdeunicorapp.home.domain.use_cases.LoadPreviousMessages
import com.markusw.cosasdeunicorapp.home.domain.use_cases.LoadPreviousNews
import com.markusw.cosasdeunicorapp.home.domain.use_cases.Logout
import com.markusw.cosasdeunicorapp.home.domain.use_cases.ObserveNewMessages
import com.markusw.cosasdeunicorapp.home.domain.use_cases.ObserveNewNews
import com.markusw.cosasdeunicorapp.home.domain.use_cases.SendMessageToGlobalChat
import com.markusw.cosasdeunicorapp.home.domain.use_cases.SendPushNotification
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
    private val downloadDocument: DownloadDocument,
    private val sendPushNotification: SendPushNotification,
    private val loadPreviousNews: LoadPreviousNews,
    private val observeNewNews: ObserveNewNews
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()
    private val homeEventsChannel = Channel<HomeEvents>()
    val homeEvents = homeEventsChannel.receiveAsFlow()
    private val chatListEventsChannel = Channel<ChatListEvent>()
    val chatListEvents = chatListEventsChannel.receiveAsFlow()
    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    private val newsListEventChannel = Channel<NewsListEvent>()
    val newsListEvents = newsListEventChannel.receiveAsFlow()

    init {
        fetchPreviousGlobalMessages()
        loadInitialNews()

        viewModelScope.launch(dispatchers.io) {
            observeNewMessages().collectLatest { newMessage ->
                _uiState.update { it.copy(globalChatList = it.globalChatList.prepend(newMessage)) }
                chatListEventsChannel.send(ChatListEvent.MessageAdded)
            }
        }

        viewModelScope.launch(dispatchers.io) {
            observeNewNews().collectLatest { newNews ->
                _uiState.update { it.copy(newsList = it.newsList.prepend(newNews)) }
                newsListEventChannel.send(NewsListEvent.NewsAdded)
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
                val repliedMessage = uiState.value.repliedMessage

                viewModelScope.launch {
                    sendMessageToGlobalChat(
                        Message(
                            content = MessageContent(
                                text = message,
                                replyTo = repliedMessage
                            ),
                            sender = sender,
                            timestamp = TimeUtils.getDeviceHourInTimestamp()
                        ).also { viewModelScope.launch { sendPushNotification(it) } }
                    )
                }

                resetMessageField()
            }

            is HomeUiEvent.ClearReplyMessage -> {
                _uiState.update { it.copy(repliedMessage = null) }
            }

            is HomeUiEvent.ReplyToMessage -> {
                _uiState.update { it.copy(repliedMessage = event.message) }
            }

            is HomeUiEvent.DownloadDocument -> {
                viewModelScope.launch {

                    _uiState.update { it.copy(isDownloadingDocument = true) }

                    when (val downloadResult = downloadDocument(event.fileName)) {
                        is Result.Error -> {

                        }

                        is Result.Success -> {
                            Timber.d("Document downloaded successfully")
                        }
                    }

                    _uiState.update { it.copy(isDownloadingDocument = false) }
                }
            }

            is HomeUiEvent.FetchPreviousNews -> fetchPreviousNews()
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

    private fun fetchPreviousNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingPreviousNews = true) }
            loadPreviousNews().also { previousNews ->
                _uiState.update {
                    it.copy(
                        newsList = it.newsList + previousNews
                    )
                }
            }
            _uiState.update { it.copy(isFetchingPreviousNews = false) }
        }
    }

    private fun loadInitialNews() {
        _uiState.update { it.copy(isFetchingPreviousNews = true) }
        viewModelScope.launch {
            loadPreviousNews().also { previousNews ->
                _uiState.update {
                    it.copy(
                        newsList = it.newsList + previousNews
                    )
                }
            }
            _uiState.update { it.copy(isFetchingPreviousNews = false) }
        }
    }

    private suspend fun handleLogoutResult(result: Result<Unit>) {
        when (result) {
            is Result.Error -> {

            }

            is Result.Success -> {
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
        chatListEventsChannel.close()
    }

}