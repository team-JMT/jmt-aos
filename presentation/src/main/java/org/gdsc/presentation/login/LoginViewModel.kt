package org.gdsc.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LoginViewModel : ViewModel() {

    private var _nicknameState = MutableStateFlow(EMPTY)
    val nicknameState = _nicknameState.asStateFlow()

    val isNicknameVerified: StateFlow<Boolean>
    get() = nicknameState.map { nickname ->
        nickname.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    fun updateNicknameState(nickname: String) {
        _nicknameState.value = nickname
    }

    companion object {
        private const val EMPTY = ""
    }

}