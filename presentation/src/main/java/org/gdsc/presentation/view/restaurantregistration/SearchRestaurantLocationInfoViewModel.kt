package org.gdsc.presentation.view.restaurantregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.usecase.CheckRestaurantRegistrationUseCase
import org.gdsc.domain.usecase.GetRestaurantLocationInfoUseCase
import org.gdsc.presentation.JmtLocationManager
import javax.inject.Inject

@HiltViewModel
class SearchRestaurantLocationInfoViewModel @Inject constructor(
    private val locationManager: JmtLocationManager,
    private val getRestaurantLocationInfoUseCase: GetRestaurantLocationInfoUseCase,
    private val checkRestaurantRegistrationUseCase: CheckRestaurantRegistrationUseCase
) : ViewModel() {

    suspend fun getRestaurantLocationInfo(
        query: String, latitude: String,
        longitude: String, page: Int
    ): List<RestaurantLocationInfo> {
        return withContext(viewModelScope.coroutineContext) {
            val response = getRestaurantLocationInfoUseCase.invoke(query, latitude, longitude, page)
            response
        }
    }

    suspend fun canRegisterRestaurant(kakaoSubId: String): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            val response = checkRestaurantRegistrationUseCase.invoke(kakaoSubId)
            response
        }
    }

    suspend fun getCurrentLocation() = locationManager.getCurrentLocation()

}