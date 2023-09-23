package org.gdsc.presentation.view.webview

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gdsc.domain.usecase.token.GetAccessTokenUseCase
import javax.inject.Inject

@HiltViewModel
class SpecificWebViewViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ViewModel() {

    suspend fun getAccessToken() = getAccessTokenUseCase.invoke()
}