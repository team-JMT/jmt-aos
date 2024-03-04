package org.gdsc.presentation.view.allsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.usecase.GetRestaurantBySearchUseCase
import org.gdsc.presentation.JmtLocationManager
import javax.inject.Inject

@HiltViewModel
class AllSearchViewModel @Inject constructor(
    private val locationManager: JmtLocationManager,
    private val getRestaurantBySearchUseCase: GetRestaurantBySearchUseCase,
): ViewModel(){

    private var _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String>
        get() = _searchKeyword

    private var _sortTypeState = MutableStateFlow(SortType.DISTANCE)
    val sortTypeState: StateFlow<SortType>
        get() = _sortTypeState

    private var _foodCategoryState = MutableStateFlow(FoodCategory.INIT)
    val foodCategoryState: StateFlow<FoodCategory>
        get() = _foodCategoryState

    private var _drinkPossibilityState = MutableStateFlow(DrinkPossibility.INIT)
    val drinkPossibilityState: StateFlow<DrinkPossibility>
        get() = _drinkPossibilityState


    fun setSearchKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun setSortType(sortType: SortType) {
        _sortTypeState.value = sortType
    }

    fun setFoodCategory(foodCategory: FoodCategory) {
        _foodCategoryState.value = foodCategory
    }

    fun setDrinkPossibility(drinkPossibility: DrinkPossibility) {
        _drinkPossibilityState.value = drinkPossibility
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registeredPagingData(): Flow<PagingData<RegisteredRestaurant>> {
        val location = locationManager.getCurrentLocation() ?: return flowOf(PagingData.empty())
        val userLoc = Location(location.longitude.toString(), location.latitude.toString())

        return run {
            getRestaurantBySearchUseCase(searchKeyword.value, userLoc).distinctUntilChanged()
        }.cachedIn(viewModelScope)
    }
}