package org.gdsc.presentation.view.restaurantregistration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.gdsc.domain.Empty
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.model.RestaurantDetailInfo
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.usecase.PostRestaurantInfoUseCase
import org.gdsc.domain.usecase.PostRestaurantLocationInfoUseCase
import org.gdsc.presentation.model.FoodCategoryItem
import javax.inject.Inject

@HiltViewModel
class RegisterRestaurantViewModel @Inject constructor(
    private val postRestaurantLocationInfoUseCase: PostRestaurantLocationInfoUseCase,
    private val postRestaurantInfoUseCase: PostRestaurantInfoUseCase
) : ViewModel() {

    private var _foodCategoryState = MutableStateFlow(FoodCategoryItem.INIT)
    val foodCategoryState = _foodCategoryState.asStateFlow()

    private var _drinkPossibilityState = MutableStateFlow(false)
    val drinkPossibilityState = _drinkPossibilityState.asStateFlow()

    private var _recommendDrinkTextState = MutableStateFlow(String.Empty)
    val recommendDrinkTextState = _recommendDrinkTextState.asStateFlow()

    private var _recommendMenuTextState = MutableStateFlow(String.Empty)
    val recommendMenuTextState = _recommendMenuTextState.asStateFlow()

    private var _introductionTextState = MutableStateFlow(String.Empty)
    val introductionTextState = _introductionTextState.asStateFlow()

    private var _recommendMenuListState: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    private val recommendMenuListState = _recommendMenuListState.asStateFlow()

    val isRecommendMenuFullState: StateFlow<Boolean>
        get() = recommendMenuListState.map {
            it.size >= 6
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    private var _isImageButtonExtended = MutableStateFlow(true)
    val isImageButtonExtended = _isImageButtonExtended.asStateFlow()

    private var _isImageButtonAnimating = MutableStateFlow(false)
    val isImageButtonAnimating = _isImageButtonAnimating.asStateFlow()

    fun setFoodCategoryState(foodCategoryItem: FoodCategoryItem) {
        _foodCategoryState.value = foodCategoryItem
    }

    fun setDrinkPossibilityState() {
        _drinkPossibilityState.value = _drinkPossibilityState.value.not()
    }

    fun setRecommendDrinkTextState(text: String) {
        _recommendDrinkTextState.value = text
    }

    fun setIntroductionTextState(text: String) {
        _introductionTextState.value = text
    }

    fun setRecommendMenuTextState(text: String) {
        _recommendMenuTextState.value = text
    }

    fun setIsImageButtonExtended(isExtended: Boolean) {
        _isImageButtonExtended.value = isExtended
        setImageButtonAnimatingTime()
    }

    fun addRecommendMenu(text: String) {
        _recommendMenuListState.value = _recommendMenuListState.value + text
    }

    private fun setImageButtonAnimatingTime(animationTime: Long = 300L) {
        viewModelScope.launch {
            _isImageButtonAnimating.value = true
            delay(animationTime)
            _isImageButtonAnimating.value = false
        }
    }

    fun setRestaurantDetailInfo(detailData : RestaurantDetailInfo) {
        _foodCategoryState.value = FoodCategoryItem(FoodCategory.fromId(detailData.categoryId))
        _drinkPossibilityState.value = detailData.canDrinkLiquor
        _recommendDrinkTextState.value = detailData.goWellWithLiquor
        _recommendMenuListState.value = detailData.recommendMenu.split(" ")
        _introductionTextState.value = detailData.introduce
    }

    fun registerRestaurant(
        pictures: List<MultipartBody.Part>,
        restaurantLocationInfo: RestaurantLocationInfo,
        actionAfterRegisterSuccess: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val restaurantLocationInfoId = postRestaurantLocationInfoUseCase(restaurantLocationInfo)

            val restaurantId = postRestaurantInfoUseCase(
                name = restaurantLocationInfo.placeName,
                introduce = introductionTextState.value,
                categoryId = foodCategoryState.value.categoryItem.id,
                pictures = pictures,
                canDrinkLiquor = _drinkPossibilityState.value,
                goWellWithLiquor = recommendDrinkTextState.value,
                recommendMenu = recommendMenuListState.value.joinToString(" ") {
                    "#$it"
                },
                restaurantLocationAggregateIdg = restaurantLocationInfoId
            )

            actionAfterRegisterSuccess(restaurantId)
        }
    }

}