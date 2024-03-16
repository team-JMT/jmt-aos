package org.gdsc.presentation.view.mypage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.gdsc.domain.model.Review
import org.gdsc.domain.model.UserInfo
import org.gdsc.domain.model.response.RestaurantInfoResponse
import org.gdsc.domain.usecase.GetRestaurantInfoUseCase
import org.gdsc.domain.usecase.GetRestaurantReviewsUseCase
import org.gdsc.domain.usecase.PostReviewUseCase
import org.gdsc.domain.usecase.user.GetOtherUserInfoUseCase
import org.gdsc.presentation.JmtLocationManager
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel
@Inject constructor(
    private val jmtLocationManager: JmtLocationManager,
    private val getRestaurantInfoUseCase: GetRestaurantInfoUseCase,
    private val getOtherUserInfoUseCase: GetOtherUserInfoUseCase,
    private val getRestaurantReviewsUseCase: GetRestaurantReviewsUseCase,
    private val postReviewUseCase: PostReviewUseCase
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

    private var _photosForReviewState: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val photosForReviewState = _photosForReviewState.asStateFlow()

    fun setPhotosForReviewState(images: List<String>) {
        _photosForReviewState.value = images
    }

    fun deletePhotoForReviewState(image: String) {
        _photosForReviewState.value = _photosForReviewState.value - image
    }

    fun init(id:Int) {
        viewModelScope.launch {
            val currentLocation = jmtLocationManager.getCurrentLocation()
            val restaurantInfo = getRestaurantInfoUseCase(id, currentLocation?.longitude.toString(), currentLocation?.latitude.toString())
            _restaurantInfo.value = restaurantInfo

            val userInfo = getOtherUserInfoUseCase(restaurantInfo.userId)
            _authorInfo.value = userInfo

            val reviews = getRestaurantReviewsUseCase(id)
            _reviews.value = reviews
        }
    }

//    init {
//        viewModelScope.launch {
//
//            val currentLocation = jmtLocationManager.getCurrentLocation()
//            val restaurantInfo = getRestaurantInfoUseCase(state.value, currentLocation?.longitude.toString(), currentLocation?.latitude.toString())
//            _restaurantInfo.value = restaurantInfo
//
//            val userInfo = getOtherUserInfoUseCase(restaurantInfo.userId)
//            _authorInfo.value = userInfo
//
//            val reviews = getRestaurantReviewsUseCase(1)
//            _reviews.value = reviews
//
//        }
//
//    }

    fun postReview(content: String, pictures: List<MultipartBody.Part>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val isSuccess = postReviewUseCase(1, content, pictures)

            if (isSuccess) {
                _photosForReviewState.value = emptyList()
                onSuccess()
            }
        }
    }

    fun onDataCleared() {
        _restaurantInfo.value = null
        _authorInfo.value = null
        _reviews.value = emptyList()
    }

}