package com.example.dirverapp.ui.list

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dirverapp.utils.UpdateLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor(
    private val updateLocation: UpdateLocation,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    fun getLocation() = viewModelScope.launch {
        _isLoading.value = true
        updateLocation.getCurrentLocation().collect { lastLocation ->
            _currentLocation.value = lastLocation
            cancel("stop location updates")
        }
        _isLoading.value = false
    }
}
