package com.markusw.cosasdeunicorapp.home.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.domain.AppSound
import com.markusw.cosasdeunicorapp.core.domain.LocalDataStore
import com.markusw.cosasdeunicorapp.core.domain.RemoteDatabase
import com.markusw.cosasdeunicorapp.core.domain.SoundPlayer
import com.markusw.cosasdeunicorapp.core.ext.prepend
import com.markusw.cosasdeunicorapp.core.utils.Result
import com.markusw.cosasdeunicorapp.core.utils.TimeUtils
import com.markusw.cosasdeunicorapp.home.domain.model.Message
import com.markusw.cosasdeunicorapp.home.domain.model.MessageContent
import com.markusw.cosasdeunicorapp.home.domain.remote.PushNotificationService
import com.markusw.cosasdeunicorapp.home.domain.use_cases.AddUserToLikedByList
import com.markusw.cosasdeunicorapp.home.domain.use_cases.DownloadDocument
import com.markusw.cosasdeunicorapp.home.domain.use_cases.GetUsersCount
import com.markusw.cosasdeunicorapp.home.domain.use_cases.LoadPreviousMessages
import com.markusw.cosasdeunicorapp.home.domain.use_cases.LoadPreviousNews
import com.markusw.cosasdeunicorapp.home.domain.use_cases.Logout
import com.markusw.cosasdeunicorapp.home.domain.use_cases.ObserveNewMessages
import com.markusw.cosasdeunicorapp.home.domain.use_cases.ObserveNewNews
import com.markusw.cosasdeunicorapp.home.domain.use_cases.RemoveUserFromLikedByList
import com.markusw.cosasdeunicorapp.home.domain.use_cases.SendMessageToGlobalChat
import com.markusw.cosasdeunicorapp.home.domain.use_cases.SendPushNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val observeNewMessages: ObserveNewMessages,
    private val loadPreviousMessages: LoadPreviousMessages,
    private val sendMessageToGlobalChat: SendMessageToGlobalChat,
    private val logout: Logout,
    private val downloadDocument: DownloadDocument,
    private val sendPushNotification: SendPushNotification,
    private val loadPreviousNews: LoadPreviousNews,
    private val observeNewNews: ObserveNewNews,
    private val addUserToLikedByList: AddUserToLikedByList,
    private val removeUserFromLikedByList: RemoveUserFromLikedByList,
    private val getUsersCount: GetUsersCount,
    private val localDataStore: LocalDataStore,
    private val pushNotificationService: PushNotificationService,
    private val remoteDatabase: RemoteDatabase,
    private val soundPlayer: SoundPlayer
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

    companion object {
        const val GENERAL_CHAT_NOTIFICATIONS_KEY = "isGeneralChatNotificationsEnabled"
        const val NEWS_NOTIFICATIONS_KEY = "isNewsNotificationsEnabled"
        const val DARK_MODE_KEY = "isDarkModeEnabled"
        const val APP_SOUNDS_KEY = "isAppSoundsEnabled"
    }

    init {
        loadInitialData()

        viewModelScope.launch(dispatchers.io) {
            observeNewMessages().collectLatest { newMessage ->
                _uiState.update { it.copy(globalChatList = it.globalChatList.prepend(newMessage)) }
                chatListEventsChannel.send(ChatListEvent.MessageAdded)

                if (newMessage.sender.uid != uiState.value.currentUser.uid) {
                    playSound(AppSound.MessageReceived)
                }
            }
        }

        viewModelScope.launch(dispatchers.io) {
            observeNewNews().collectLatest { newNews ->
                _uiState.update { it.copy(newsList = it.newsList.prepend(newNews)) }
                newsListEventChannel.send(NewsListEvent.NewsAdded)
            }
        }

        viewModelScope.launch(dispatchers.io) {
            remoteDatabase.onUserInfoUpdate().collectLatest { updatedUser ->
                _uiState.update { it.copy(currentUser = updatedUser) }
            }

        }

        viewModelScope.launch(dispatchers.io) {
            localDataStore
                .get(GENERAL_CHAT_NOTIFICATIONS_KEY)
                .collectLatest { isGeneralChatNotificationsEnabled ->
                    _uiState.update { state ->
                        state.copy(
                            localSettings = state.localSettings.copy(
                                isGeneralChatNotificationsEnabled = isGeneralChatNotificationsEnabled?.toBoolean()
                                    ?: true
                            )
                        )
                    }
                }
        }

        viewModelScope.launch(dispatchers.io) {
            localDataStore
                .get(DARK_MODE_KEY)
                .collectLatest { isDarkModeEnabled ->
                    _uiState.update { state ->
                        state.copy(
                            localSettings = state.localSettings.copy(
                                isDarkModeEnabled = isDarkModeEnabled?.toBoolean()
                                    ?: false
                            )
                        )
                    }
                }
        }

        viewModelScope.launch(dispatchers.io) {
            localDataStore
                .get(APP_SOUNDS_KEY)
                .collectLatest { isAppSoundsEnabled ->
                    _uiState.update { state ->
                        state.copy(
                            localSettings = state.localSettings.copy(
                                isAppSoundsEnabled = isAppSoundsEnabled?.toBoolean()
                                    ?: true
                            )
                        )
                    }
                }
        }

        viewModelScope.launch(dispatchers.io) {
            localDataStore
                .get(NEWS_NOTIFICATIONS_KEY)
                .collectLatest { isNewsNotificationsEnabled ->
                    _uiState.update { state ->
                        state.copy(
                            localSettings = state.localSettings.copy(
                                isNewsNotificationsEnabled = isNewsNotificationsEnabled?.toBoolean()
                                    ?: true
                            )
                        )
                    }
                }
        }

    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.FetchPreviousGlobalMessages -> {
                fetchPreviousGlobalMessages()
            }

            is HomeUiEvent.CloseSession -> {
                _uiState.update {
                    it.copy(isClosingSession = true)
                }
                viewModelScope.launch(dispatchers.io) {
                    val authResult = logout()
                    handleLogoutResult(authResult)
                    _uiState.update {
                        it.copy(isClosingSession = false)
                    }
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
                        ).also {
                            viewModelScope.launch {
                                playSound(AppSound.MessageSent)
                                sendPushNotification(it)
                            }
                        }
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

                    delay(2500)
                    when (downloadDocument(event.fileName)) {
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

            is HomeUiEvent.LikeNews -> {

                val isUserInLikedList = event.news.likedBy.contains(uiState.value.currentUser.uid)

                if (isUserInLikedList) {

                    val newsIndex = uiState.value.newsList.indexOf(event.news)
                    val updatedNews = event.news.copy(
                        likedBy = event.news.likedBy.filter { it != uiState.value.currentUser.uid }
                    )
                    _uiState.update {
                        it.copy(
                            newsList = it.newsList.toMutableList().also { newsList ->
                                newsList[newsIndex] = updatedNews
                            }
                        )
                    }

                    viewModelScope.launch(dispatchers.io) {
                        removeUserFromLikedByList(event.news.id!!, uiState.value.currentUser.uid)
                    }
                } else {
                    val newsIndex = uiState.value.newsList.indexOf(event.news)
                    val updatedNews = event.news.copy(
                        likedBy = event.news.likedBy + uiState.value.currentUser.uid
                    )
                    _uiState.update {
                        it.copy(
                            newsList = it.newsList.toMutableList().also { newsList ->
                                newsList[newsIndex] = updatedNews
                            }
                        )
                    }

                    viewModelScope.launch(dispatchers.io) {
                        playSound(AppSound.Like)
                        addUserToLikedByList(event.news.id!!, uiState.value.currentUser.uid)
                    }
                }
            }

            is HomeUiEvent.ToggleGeneralChatNotifications -> {
                val isGeneralChatNotificationsEnabled =
                    uiState.value.localSettings.isGeneralChatNotificationsEnabled

                if (isGeneralChatNotificationsEnabled) {
                    pushNotificationService.disableGeneralChatNotifications()
                } else {
                    pushNotificationService.enableGeneralChatNotifications()
                }

                viewModelScope.launch(dispatchers.io) {
                    localDataStore.save(
                        GENERAL_CHAT_NOTIFICATIONS_KEY,
                        (!isGeneralChatNotificationsEnabled).toString()
                    )
                }
            }

            is HomeUiEvent.ToggleNewsNotifications -> {
                val isNewsNotificationsEnabled =
                    uiState.value.localSettings.isNewsNotificationsEnabled

                if (isNewsNotificationsEnabled) {
                    pushNotificationService.disableNewsNotifications()
                } else {
                    pushNotificationService.enableNewsNotifications()
                }

                viewModelScope.launch(dispatchers.io) {
                    localDataStore.save(
                        NEWS_NOTIFICATIONS_KEY,
                        (!isNewsNotificationsEnabled).toString()
                    )
                }
            }

            is HomeUiEvent.ChangeDocumentName -> {
                _uiState.update { it.copy(documentName = event.documentName) }
            }

            is HomeUiEvent.ChangeSearchBarActive -> {
                _uiState.update { it.copy(isDocumentSearchBarActive = event.isActive) }
            }

            is HomeUiEvent.SearchDocument -> {
                val searchedDocuments = uiState
                    .value
                    .documentsList
                    .filter { it.name.contains(event.query, ignoreCase = true) }

                _uiState.update {
                    it.copy(
                        searchedDocumentsList = searchedDocuments
                    )
                }
            }

            is HomeUiEvent.ChangeDarkMode -> {
                viewModelScope.launch(dispatchers.io) {
                    localDataStore.save(
                        DARK_MODE_KEY,
                        event.isDarkMode.toString()
                    )
                }
            }

            is HomeUiEvent.ToggleAppSounds -> {
                viewModelScope.launch(dispatchers.io) {
                    localDataStore.save(
                        APP_SOUNDS_KEY,
                        event.isSoundsEnabled.toString()
                    )
                }
            }
        }
    }

    private fun loadInitialData() {
        _uiState.update { state ->
            state.copy(
                isFetchingPreviousNews = true,
                isFetchingPreviousGlobalMessages = true,
                isLoading = true
            )
        }

        viewModelScope.launch(dispatchers.io) {
            val initialNews = loadPreviousNews()
            val initialMessages = loadPreviousMessages()
            val usersCount = when (val result = getUsersCount()) {
                is Result.Error -> 0
                is Result.Success -> result.data
            }

            withContext(dispatchers.main) {
                _uiState.update { state ->
                    state.copy(
                        globalChatList = state.globalChatList + initialMessages,
                        newsList = state.newsList + initialNews,
                        isFetchingPreviousGlobalMessages = false,
                        isFetchingPreviousNews = false,
                        usersCount = usersCount ?: 0,
                        isLoading = false
                    )
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


    private suspend fun handleLogoutResult(result: Result<Unit>) {
        when (result) {
            is Result.Error -> {
                Timber.d("Error logging out: ${result.message}")
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

    private fun playSound(sound: AppSound) {
        if (uiState.value.localSettings.isAppSoundsEnabled) {
            soundPlayer.playSound(sound)
        }
    }

    override fun onCleared() {
        super.onCleared()
        homeEventsChannel.close()
        chatListEventsChannel.close()
    }

}