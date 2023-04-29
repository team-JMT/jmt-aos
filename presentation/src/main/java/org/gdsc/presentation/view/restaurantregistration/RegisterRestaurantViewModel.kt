package org.gdsc.presentation.view.restaurantregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterRestaurantViewModel @Inject constructor() : ViewModel() {

    private var _drinkPossibilityState = MutableStateFlow(false)
    val drinkPossibilityState = _drinkPossibilityState.asStateFlow()

    private var _introductionTextState = MutableStateFlow("")
    val introductionTextState = _introductionTextState.asStateFlow()

    private var _isImageButtonExtended = MutableStateFlow(true)
    val isImageButtonExtended = _isImageButtonExtended.asStateFlow()

    private var _isImageButtonAnimating = MutableStateFlow(false)
    val isImageButtonAnimating = _isImageButtonAnimating.asStateFlow()

    fun setDrinkPossibilityState() {
        _drinkPossibilityState.value = _drinkPossibilityState.value.not()
    }

    fun setIntroductionTextState(text: String) {
        _introductionTextState.value = text
    }

    fun setIsImageButtonExtended(isExtended: Boolean) {
        _isImageButtonExtended.value = isExtended
        setImageButtonAnimatingTime()
    }

    private fun setImageButtonAnimatingTime(animationTime: Long = 300L) {
        viewModelScope.launch {
            _isImageButtonAnimating.value = true
            delay(animationTime)
            _isImageButtonAnimating.value = false
        }
    }

}