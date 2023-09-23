package org.gdsc.presentation.view.home

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gdsc.domain.model.Location
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

    suspend fun setUserPosition(): String {

        val currentLocation = getCurrentLocation() ?: return ""
        val location = currentLocation.let {
            Location(y = it.latitude.toString(), x = it.longitude.toString())
        }

        return Gson().toJson(location)
    }
}