package org.gdsc.presentation.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.naver.maps.geometry.LatLng
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
import org.gdsc.domain.model.response.Group
import org.gdsc.domain.usecase.GetMyGroupUseCase
import org.gdsc.domain.usecase.GetRestaurantsByMapUseCase
import org.gdsc.domain.usecase.PostSelectGroupUseCase
import org.gdsc.presentation.JmtLocationManager
import org.gdsc.presentation.model.ResultState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationManager: JmtLocationManager,
    private val getRestaurantsByMapUseCase: GetRestaurantsByMapUseCase,
    private val getMyGroupUseCase: GetMyGroupUseCase,
    private val postSelectGroupUserCase: PostSelectGroupUseCase,
) : ViewModel() {

    suspend fun getCurrentLocation() = locationManager.getCurrentLocation()

    private var _userLocationState = MutableStateFlow(Location("0", "0"))
    val userLocationState: StateFlow<Location>
        get() = _userLocationState


    private var _startLocationState = MutableStateFlow(Location("0", "0"))
    val startLocationState: StateFlow<Location>
        get() = _startLocationState


    private var _endLocationState = MutableStateFlow(Location("0", "0"))
    val endLocationState: StateFlow<Location>
        get() = _endLocationState

    private var _sortTypeState = MutableStateFlow(SortType.DISTANCE)
    val sortTypeState: StateFlow<SortType>
        get() = _sortTypeState

    private var _foodCategoryState = MutableStateFlow(FoodCategory.INIT)
    val foodCategoryState: StateFlow<FoodCategory>
        get() = _foodCategoryState

    private var _drinkPossibilityState = MutableStateFlow(DrinkPossibility.INIT)
    val drinkPossibilityState: StateFlow<DrinkPossibility>
        get() = _drinkPossibilityState

    private var _myGroupList = MutableStateFlow<ResultState<List<Group>>>(ResultState.OnLoading())
    val myGroupList: StateFlow<ResultState<List<Group>>>
        get() = _myGroupList


    private var _currentGroup = MutableStateFlow<ResultState<Group?>>(ResultState.OnLoading())
    val currentGroup: StateFlow<ResultState<Group?>>
        get() = _currentGroup


    fun setUserLocation(userLocation: Location) {
        _userLocationState.value = userLocation
    }

    fun setStartLocation(startLocation: Location) {
        _startLocationState.value = startLocation
    }

    fun setEndLocation(endLocation: Location) {
        _endLocationState.value = endLocation
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

    fun setGroupList(groupList: List<Group>) {
        _myGroupList.value = ResultState.OnSuccess(groupList)
    }

    fun setCurrentGroup(group: Group?) {
        _currentGroup.value = ResultState.OnSuccess(group)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registeredPagingDataByMap(): Flow<PagingData<RegisteredRestaurant>> {
        val location = locationManager.getCurrentLocation() ?: return flowOf(PagingData.empty())
        val userLoc = Location(location.longitude.toString(), location.latitude.toString())

        return run {
            return@run combine(
                startLocationState,
                endLocationState,
                sortTypeState,
                foodCategoryState,
                drinkPossibilityState
            ) { startLoc, endLoc, sortType, foodCategory, drinkPossibility ->
                getRestaurantsByMapUseCase(sortType, foodCategory, drinkPossibility, userLoc, startLoc, endLoc)
            }.distinctUntilChanged()
                .flatMapLatest { it }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registeredPagingDataByGroup(): Flow<PagingData<RegisteredRestaurant>> {

        return run {
            return@run combine(
                userLocationState,
                sortTypeState,
                foodCategoryState,
                drinkPossibilityState
            ) { userLoc, sortType, foodCategory, drinkPossibility ->
                getRestaurantsByMapUseCase(sortType, foodCategory, drinkPossibility, userLoc, null, null)
            }.distinctUntilChanged()
                .flatMapLatest { it }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registeredPagingDataByGroup(): Flow<PagingData<RegisteredRestaurant>> {

        return run {
            return@run combine(
                userLocationState,
                sortTypeState,
                foodCategoryState,
                drinkPossibilityState
            ) { userLoc, sortType, foodCategory, drinkPossibility ->
                getRestaurantsByMapUseCase(sortType, foodCategory, drinkPossibility, userLoc, null, null)
            }.distinctUntilChanged()
                .flatMapLatest { it }
        }.cachedIn(viewModelScope)
    }

    suspend fun getMyGroup(): List<Group> {
        return getMyGroupUseCase()
    }

    suspend fun selectGroup(groupID: Int) {
        postSelectGroupUserCase(groupID)
    }
}