package org.gdsc.presentation.view.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gdsc.presentation.JmtLocationManager
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationManager: JmtLocationManager
) : ViewModel() {

    suspend fun getCurrentLocation() = locationManager.getCurrentLocation()
}