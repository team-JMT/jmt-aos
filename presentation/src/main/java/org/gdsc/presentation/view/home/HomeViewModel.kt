package org.gdsc.presentation.view.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gdsc.domain.usecase.token.GetAccessTokenUseCase
import org.gdsc.presentation.JmtLocationManager
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationManager: JmtLocationManager,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ViewModel() {

    suspend fun getCurrentLocation() = locationManager.getCurrentLocation()

    suspend fun getAccessToken() = getAccessTokenUseCase.invoke()
}