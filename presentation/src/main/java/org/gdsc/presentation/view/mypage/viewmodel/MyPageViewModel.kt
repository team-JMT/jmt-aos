package org.gdsc.presentation.view.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.gdsc.domain.Empty
import org.gdsc.domain.SortType
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.response.NicknameResponse
import org.gdsc.domain.usecase.CheckDuplicatedNicknameUseCase
import org.gdsc.domain.usecase.GetRegisteredRestaurantUseCase
import org.gdsc.domain.usecase.PostNicknameUseCase
import org.gdsc.domain.usecase.PostUserLogoutUseCase
import org.gdsc.domain.usecase.PostUserSignoutUseCase
import org.gdsc.domain.usecase.token.ClearTokenInfoUseCase
import org.gdsc.domain.usecase.token.GetRefreshTokenUseCase
import org.gdsc.domain.usecase.user.GetUserInfoUseCase
import org.gdsc.domain.usecase.user.PostDefaultProfileImageUseCase
import org.gdsc.domain.usecase.user.PostProfileImageUseCase
import org.gdsc.presentation.model.ResultState
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userInfoUseCase: GetUserInfoUseCase,
    private val postNicknameUseCase: PostNicknameUseCase,
    private val checkDuplicatedNicknameUseCase: CheckDuplicatedNicknameUseCase,
    private val postProfileImageUseCase: PostProfileImageUseCase,
    private val postDefaultProfileImageUseCase: PostDefaultProfileImageUseCase,
    private val postUserLogoutUseCase: PostUserLogoutUseCase,
    private val getRefreshTokenUseCase: GetRefreshTokenUseCase,
    private val clearTokenInfoUseCase: ClearTokenInfoUseCase,
    private val postUserSignoutUseCase: PostUserSignoutUseCase,
): ViewModel() {

    private var _sortTypeState = MutableStateFlow(SortType.INIT)
    val sortTypeState: StateFlow<SortType>
        get() = _sortTypeState

    private var _idState = MutableStateFlow<Int?>(null)
    val idState: StateFlow<Int?> = _idState.asStateFlow()

    private var _nicknameState = MutableStateFlow(String.Empty)
    val nicknameState = _nicknameState.asStateFlow()

    private var _profileImageState = MutableStateFlow(String.Empty)
    val profileImageState = _profileImageState.asStateFlow()

    private var _emailState = MutableStateFlow(String.Empty)
    val emailState = _emailState.asStateFlow()

    suspend fun getUserInfo() {
        return withContext(viewModelScope.coroutineContext) {
            val response = userInfoUseCase.invoke()

            _idState.value = response.id
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

    fun updateProfileImage(file: MultipartBody.Part, callback: (ResultState<String>) -> Unit) {
        viewModelScope.launch {
            postProfileImageUseCase(file).let {
                when(it.code) {
                    "PROFILE_IMAGE_UPDATE_SUCCESS" -> callback(ResultState.onSuccess(it.data))

                    "INTERNAL_SERVER_ERROR" -> callback(ResultState.onError(it.code, it.message))

                    else -> callback(ResultState.onError(it.code, it.message))
                }
            }

        }
    }

    fun updateDefaultProfileImage(callback: (ResultState<String>) -> Unit) {
        viewModelScope.launch {
            postDefaultProfileImageUseCase().let {

                when(it.code) {
                    "PROFILE_IMAGE_UPDATE_SUCCESS" -> callback(ResultState.onSuccess(it.data))

                    "INTERNAL_SERVER_ERROR" -> callback(ResultState.onError(it.code, it.message))

                    else -> callback(ResultState.onError(it.code, it.message))
                }
            }
        }
    }

    fun logout(onCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val refreshToken = getRefreshTokenUseCase.invoke()

            val response = async { postUserLogoutUseCase(
                refreshToken
            ) }

            when(response.await()) {
                "LOGOUT_SUCCESS" -> {
                    clearTokenInfoUseCase.invoke()
                    onCallback.invoke()
                }
                "REISSUE_FAIL" -> {
                    Log.e("MyPageViewModel", "RefreshToken이 유효하지 않습니다.")
                }
                "UNAUTHORIZED" -> {
                    Log.e("MyPageViewModel", "인증이 필요합니다.")
                }

                else -> {
                    Log.e("MyPageViewModel", "알 수 없는 오류가 발생했습니다.")
                }
            }

            onCallback()
        }
    }

    fun signout(onCallback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO){
            val response = async { postUserSignoutUseCase.invoke() }

            when(response.await()) {
                "USER_REMOVE_SUCCESS" -> {
                    clearTokenInfoUseCase.invoke()
                    onCallback.invoke()
                }
                "UNAUTHORIZED" -> {
                    Log.e("MyPageViewModel", "인증이 필요합니다.")
                }
                else -> {
                    Log.e("MyPageViewModel", "알 수 없는 오류가 발생했습니다.")
                }
            }
        }
    }

    private var _myRegisteredRestaurantState = MutableStateFlow(String.Empty)
    val myRegisteredRestaurantState = _myRegisteredRestaurantState.asStateFlow()


}