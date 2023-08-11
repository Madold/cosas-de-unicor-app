package com.markusw.cosasdeunicorapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markusw.cosasdeunicorapp.core.DispatcherProvider
import com.markusw.cosasdeunicorapp.core.utils.Resource
import com.markusw.cosasdeunicorapp.domain.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dispatchers: DispatcherProvider,
    private val authService: AuthService
) : ViewModel() {

    private val homeEventsChannel = Channel<HomeEvents>()
    val homeEvents = homeEventsChannel.receiveAsFlow()

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


}