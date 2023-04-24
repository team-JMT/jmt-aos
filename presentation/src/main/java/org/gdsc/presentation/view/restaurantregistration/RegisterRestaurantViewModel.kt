package org.gdsc.presentation.view.restaurantregistration

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterRestaurantViewModel @Inject constructor() : ViewModel() {

    private var _drinkPossibilityState = MutableStateFlow(false)
    val drinkPossibilityState = _drinkPossibilityState.asStateFlow()

    fun setDrinkPossibilityState() {
        _drinkPossibilityState.value = _drinkPossibilityState.value.not()
    }

}