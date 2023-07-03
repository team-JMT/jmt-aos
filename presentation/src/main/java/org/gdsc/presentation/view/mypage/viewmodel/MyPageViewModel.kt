package org.gdsc.presentation.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import org.gdsc.domain.SortType
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.usecase.user.GetUserInfoUseCase
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    private var _sortTypeState = MutableStateFlow(SortType.INIT)
    val sortTypeState: StateFlow<SortType>
        get() = _sortTypeState

    private var _userInfoState = MutableStateFlow(UserInfo())
    val userInfoState: StateFlow<UserInfo>
        get() = _userInfoState

    suspend fun getUserInfo() {
        return withContext(viewModelScope.coroutineContext) {
            val response = userInfoUseCase.invoke()
            _userInfoState.value = response
        }
    }
}
//GetUserInfoUseCase