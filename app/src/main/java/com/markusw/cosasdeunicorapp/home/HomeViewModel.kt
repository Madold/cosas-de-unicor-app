package com.markusw.cosasdeunicorapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.data.ChatRepository
import com.markusw.cosasdeunicorapp.data.model.Message
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val authService: AuthService,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()
    private val homeEventsChannel = Channel<HomeEvents>()
    val homeEvents = homeEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch(dispatchers.io) {
            chatRepository.getGlobalChatList().collect { response ->
                when (response) {
                    is Resource.Error -> {
                        Timber.d("Error: ${response.message}")
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(globalChatList = response.data!!) }
                    }
                }
            }
        }
    }

    fun onCloseSession() {
        viewModelScope.launch(dispatchers.io) {
            when (val authResult =  authService.logout()) {
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
        viewModelScope.launch(dispatchers.io) {
            chatRepository.sendMessageToGlobalChat(
                Message(
                    uiState.value.message,
                    "Markus",
                    System.currentTimeMillis()
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        homeEventsChannel.close()
    }

}