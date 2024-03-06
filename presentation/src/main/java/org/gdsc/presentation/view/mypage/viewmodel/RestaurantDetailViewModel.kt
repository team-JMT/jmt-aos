package org.gdsc.presentation.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdsc.domain.model.Review
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.model.response.RestaurantInfoResponse
import org.gdsc.domain.usecase.GetRestaurantInfoUseCase
import org.gdsc.domain.usecase.GetRestaurantReviewsUseCase
import org.gdsc.domain.usecase.user.GetOtherUserInfoUseCase
import org.gdsc.presentation.JmtLocationManager
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel
@Inject constructor(
    private val jmtLocationManager: JmtLocationManager,
    private val getRestaurantInfoUseCase: GetRestaurantInfoUseCase,
    private val getOtherUserInfoUseCase: GetOtherUserInfoUseCase,
    private val getRestaurantReviewsUseCase: GetRestaurantReviewsUseCase
): ViewModel() {

    private var _restaurantInfo: MutableStateFlow<RestaurantInfoResponse?> = MutableStateFlow(null)
    val restaurantInfo: StateFlow<RestaurantInfoResponse?>
        get() = _restaurantInfo

    private var _authorInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val authorInfo: StateFlow<UserInfo?>
        get() = _authorInfo

    private var _reviews: MutableStateFlow<List<Review>> = MutableStateFlow(emptyList())
    val reviews: StateFlow<List<Review>>
        get() = _reviews


    init {
        viewModelScope.launch {

            val currentLocation = jmtLocationManager.getCurrentLocation()
            val restaurantInfo = getRestaurantInfoUseCase(1, currentLocation?.longitude.toString(), currentLocation?.latitude.toString())
            _restaurantInfo.value = restaurantInfo

            val userInfo = getOtherUserInfoUseCase(restaurantInfo.userId)
            _authorInfo.value = userInfo

            val reviews = getRestaurantReviewsUseCase(1)
            _reviews.value = reviews

        }

    }

}