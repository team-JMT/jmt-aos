package org.gdsc.presentation.view.restaurantregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.domain.usecase.GetRestaurantLocationInfoUseCase
import javax.inject.Inject

@HiltViewModel
class SearchRestaurantLocationInfoViewModel @Inject constructor(
    private val getRestaurantLocationInfoUseCase: GetRestaurantLocationInfoUseCase
) : ViewModel() {

    suspend fun getRestaurantLocationInfo(query: String, page: Int): List<RestaurantLocationInfo> {
        return withContext(viewModelScope.coroutineContext) {
            val response = getRestaurantLocationInfoUseCase.invoke(query, page)
            response
        }
    }

}