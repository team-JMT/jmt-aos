package org.gdsc.presentation.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.gdsc.domain.Empty
import org.gdsc.domain.SortType
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.usecase.CheckDuplicatedNicknameUseCase
import org.gdsc.domain.usecase.PostNicknameUseCase
import org.gdsc.domain.usecase.user.GetUserInfoUseCase
import org.gdsc.domain.usecase.user.PostDefaultProfileImageUseCase
import org.gdsc.domain.usecase.user.PostProfileImageUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userInfoUseCase: GetUserInfoUseCase,
    private val postNicknameUseCase: PostNicknameUseCase,
    private val checkDuplicatedNicknameUseCase: CheckDuplicatedNicknameUseCase,
    private val postProfileImageUseCase: PostProfileImageUseCase,
    private val postDefaultProfileImageUseCase: PostDefaultProfileImageUseCase,
): ViewModel() {

    private var _sortTypeState = MutableStateFlow(SortType.INIT)
    val sortTypeState: StateFlow<SortType>
        get() = _sortTypeState

    private var _nicknameState = MutableStateFlow(String.Empty)
    val nicknameState = _nicknameState.asStateFlow()

    private var _profileImageState = MutableStateFlow(String.Empty)
    val profileImageState = _profileImageState.asStateFlow()

    private var _emailState = MutableStateFlow(String.Empty)
    val emailState = _emailState.asStateFlow()

    suspend fun getUserInfo() {
        return withContext(viewModelScope.coroutineContext) {
            val response = userInfoUseCase.invoke()

            _nicknameState.value = response.nickname
            _profileImageState.value = response.profileImg
            _emailState.value = response.email
        }
    }

    fun updateNickNameState(nickName: String) {
        _nicknameState.value = nickName
    }

    fun updateProfileImageState(profileImage: String) {
        _profileImageState.value = profileImage
    }

    fun updateUserName(nickName: String, afterSuccessCallback: (response: NicknameResponse) -> Unit) {
        viewModelScope.launch {
            val response = postNicknameUseCase.invoke(nickName)
            afterSuccessCallback(response)
        }
    }



    fun checkDuplicatedNickname(
        nickName: String,
        onNicknameIsNotDuplicated: () -> Unit = {},
        onNicknameIsDuplicated: () -> Unit = {}
    ) {
        viewModelScope.launch {
            checkDuplicatedNicknameUseCase(nickName).let { isNotDuplicated ->
                if (isNotDuplicated) {
                    onNicknameIsNotDuplicated()
                } else {
                    onNicknameIsDuplicated()
                }
            }
        }
    }

    fun updateProfileImage(file: MultipartBody.Part, afterSuccessSignUp: () -> Unit) {
        viewModelScope.launch {
            postProfileImageUseCase(file)
            afterSuccessSignUp()
        }
    }

    fun updateDefaultProfileImage(afterSuccessSignUp: () -> Unit) {
        viewModelScope.launch {
            postDefaultProfileImageUseCase()
            afterSuccessSignUp()
        }
    }
}