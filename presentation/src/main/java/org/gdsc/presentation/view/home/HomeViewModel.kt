package org.gdsc.presentation.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.gdsc.domain.DrinkPossibility
import org.gdsc.domain.FoodCategory
import org.gdsc.domain.SortType
import org.gdsc.domain.model.Location
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.ScreenLocation
import org.gdsc.domain.model.response.Group
import org.gdsc.domain.usecase.GetMyGroupUseCase
import org.gdsc.domain.usecase.GetRestaurantMapWithLimitCountUseCase
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
    private val getRestaurantMapWithLimitCountUseCase: GetRestaurantMapWithLimitCountUseCase,
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


    val _screenLocationState: StateFlow<ScreenLocation> =
        combine(
            startLocationState,
            endLocationState
        ) { startLocationState, endLocationState ->
            ScreenLocation(startLocationState, endLocationState)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ScreenLocation(Location("0", "0"), Location("0", "0")))

    val screenLocationState: StateFlow<ScreenLocation>
        get() = _screenLocationState



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


    private var _currentGroup: MutableStateFlow<Group> = MutableStateFlow(Group(0, "", "", "", "", 0, 0, false, false))
    val currentGroup: StateFlow<Group>
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

    fun setCurrentGroup(group: Group) {
        _currentGroup.value = group
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registeredPagingDataByMap(): Flow<PagingData<RegisteredRestaurant>> {
        val location = locationManager.getCurrentLocation() ?: return flowOf(PagingData.empty())
        val userLoc = Location(location.longitude.toString(), location.latitude.toString())

        return run {
            return@run combine(
                screenLocationState,
                sortTypeState,
                foodCategoryState,
                drinkPossibilityState,
                currentGroup,
            ) { screenLoc, sortType, foodCategory, drinkPossibility, group ->
                getRestaurantsByMapUseCase(sortType, foodCategory, drinkPossibility, userLoc, screenLoc.startLocation, screenLoc.endLocation, group)
            }.distinctUntilChanged()
                .flatMapLatest { it }
        }.cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registeredPagingDataByList(): Flow<PagingData<RegisteredRestaurant>> {

        return run {
            return@run combine(
                userLocationState,
                sortTypeState,
                foodCategoryState,
                drinkPossibilityState,
                currentGroup,
            ) { userLoc, sortType, foodCategory, drinkPossibility, group ->
                getRestaurantsByMapUseCase(sortType, foodCategory, drinkPossibility, userLoc, null, null, group)
            }.distinctUntilChanged()
                .flatMapLatest { it }
        }.cachedIn(viewModelScope)
    }

    suspend fun getRestaurantMapWithLimitCount(sortType: SortType, group: Group?): List<RegisteredRestaurant> {
        return getRestaurantMapWithLimitCountUseCase(sortType, group)
    }

    suspend fun getMyGroup(): List<Group> {
        return getMyGroupUseCase()
    }

    suspend fun selectGroup(groupID: Int) {
        postSelectGroupUserCase(groupID)
    }
}