package org.gdsc.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.gdsc.domain.Empty
import org.gdsc.domain.usecase.PostSignUpWithGoogleTokenUseCase
import org.gdsc.domain.usecase.token.SaveTokenUseCase
import org.gdsc.domain.usecase.user.PostProfileImageUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postSignUpWithGoogleTokenUseCase: PostSignUpWithGoogleTokenUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val postProfileImageUseCase: PostProfileImageUseCase
) : ViewModel() {

    private var _nicknameState = MutableStateFlow(String.Empty)
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

    fun postSignUpWithGoogleToken(token: String, afterSuccessSignUp: () -> Unit) {
        viewModelScope.launch {
            val response = postSignUpWithGoogleTokenUseCase.invoke(token)
            saveTokenUseCase.invoke(response)
            afterSuccessSignUp()
        }
    }

    fun postProfileImage(file: MultipartBody.Part, afterSuccessPostProfileImage: () -> Unit) {
        viewModelScope.launch {
            val response = postProfileImageUseCase(file)
            afterSuccessPostProfileImage()
        }
    }

}