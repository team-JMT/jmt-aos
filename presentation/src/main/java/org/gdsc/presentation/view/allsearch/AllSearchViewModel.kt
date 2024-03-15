package org.gdsc.presentation.view.allsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.usecase.GetGroupBySearchWithLimitCountUseCase
import org.gdsc.domain.usecase.GetRestaurantBySearchUseCase
import org.gdsc.domain.usecase.GetRestaurantBySearchWithLimitCountUseCase
import org.gdsc.domain.usecase.user.DeleteSearchedKeywordUseCase
import org.gdsc.domain.usecase.user.GetSearchedKeywordsUseCase
import org.gdsc.domain.usecase.user.InitSearchedKeywordUseCase
import org.gdsc.domain.usecase.user.UpdateSearchedKeywordUseCase
import org.gdsc.presentation.JmtLocationManager
import javax.inject.Inject

@HiltViewModel
class AllSearchViewModel @Inject constructor(
    private val locationManager: JmtLocationManager,
    private val getRestaurantBySearchUseCase: GetRestaurantBySearchUseCase,
    private val getSearchedKeywordsUseCase: GetSearchedKeywordsUseCase,
    private val updateSearchedKeywordUseCase: UpdateSearchedKeywordUseCase,
    private val deleteSearchedKeywordUseCase: DeleteSearchedKeywordUseCase,
    private val initSearchedKeywordUseCase: InitSearchedKeywordUseCase,
    private val getRestaurantBySearchWithLimitCountUseCase: GetRestaurantBySearchWithLimitCountUseCase,
    private val getGroupBySearchWithLimitCountUseCase: GetGroupBySearchWithLimitCountUseCase
) : ViewModel() {

    init {

        viewModelScope.launch {
            val keywords = getSearchedKeywordsUseCase()
            if (keywords.isNotEmpty()) {
                _searchedKeywordsState.value = keywords
            }
        }
    }

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

    private var _searchedRestaurantState =
        MutableStateFlow<PagingData<RegisteredRestaurant>>(PagingData.empty())
    val searchedRestaurantState: StateFlow<PagingData<RegisteredRestaurant>>
        get() = _searchedRestaurantState

    private var _searchedRestaurantPreviewState =
        MutableStateFlow<List<RegisteredRestaurant>>(emptyList())
    val searchedRestaurantPreviewState: StateFlow<List<RegisteredRestaurant>>
        get() = _searchedRestaurantPreviewState

    private var _searchedGroupPreviewState =
        MutableStateFlow<List<GroupPreview>>(emptyList())

    val searchedGroupPreviewState: StateFlow<List<GroupPreview>>
        get() = _searchedGroupPreviewState


    private var _searchedKeywordsState = MutableStateFlow<List<String>>(emptyList())
    val searchedKeywordsState: StateFlow<List<String>>
        get() = _searchedKeywordsState

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

    fun deleteSearchedKeyword(keyword: String) {
        viewModelScope.launch {
            _searchedKeywordsState.value = deleteSearchedKeywordUseCase(keyword)
        }
    }

    fun updateSearchedKeyword(keyword: String) {
        viewModelScope.launch {
            _searchedKeywordsState.value = updateSearchedKeywordUseCase(keyword)
        }
    }

    fun deleteAllSearchedKeyword() {
        viewModelScope.launch {
            _searchedKeywordsState.value = initSearchedKeywordUseCase()
        }
    }

    fun searchRestaurantPreviewWithKeyword() {
        viewModelScope.launch {
            val location = locationManager.getCurrentLocation()

            if (location == null) {
                _searchedRestaurantPreviewState.value = emptyList()
            } else {

                val userLoc = Location(location.longitude.toString(), location.latitude.toString())

                _searchedRestaurantPreviewState.value =
                    getRestaurantBySearchWithLimitCountUseCase(searchKeyword.value, userLoc, 3)
            }

        }
    }

    fun searchRestaurantWithKeyword() {
        viewModelScope.launch {
            val location = locationManager.getCurrentLocation()

            if (location == null) {
                _searchedRestaurantState.value = PagingData.empty()
            } else {
                val userLoc = Location(location.longitude.toString(), location.latitude.toString())

                getRestaurantBySearchUseCase(searchKeyword.value, userLoc).distinctUntilChanged()
                    .collect {
                        _searchedRestaurantState.value = it
                    }
            }
        }
    }

    fun searchGroupPreviewWithKeyword() {
        viewModelScope.launch {
            val result = getGroupBySearchWithLimitCountUseCase(searchKeyword.value, 3)
            _searchedGroupPreviewState.value = result
        }
    }


}