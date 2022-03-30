package com.sargis.khlopuzyan.overplay.ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sargis.khlopuzyan.overplay.constant.Constants
import com.sargis.khlopuzyan.overplay.repository.OverplayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OverplayViewModel @Inject constructor(private val repository: OverplayRepository) :
    ViewModel(),
    DefaultLifecycleObserver {

    private val _stateFlowTextSize = MutableStateFlow(0.0f)
    val stateFlowTextSize: StateFlow<Float> = _stateFlowTextSize

    private val _stateFlowSessionCount = MutableStateFlow(0)
    val stateFlowSessionCount: StateFlow<Int> = _stateFlowSessionCount

    init {
        viewModelScope.launch {
            var sessionCount = repository.getSessionCount()

            // first launch
            if (sessionCount == null) {
                sessionCount = 1
                repository.saveSessionCount(sessionCount)
            }

            _stateFlowSessionCount.value = sessionCount
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        viewModelScope.launch {
            val lastSavedDate = repository.getLastSavedDateTime()
            if (lastSavedDate != null) {
                val currentDate = repository.getCurrentDateTime()
                var sessionCount = repository.getSessionCount() ?: 0

                val diff = try {
                    repository.calculateTotalMinutesBetweenTwoDates(lastSavedDate, currentDate)
                } catch (e: Exception) {
                    null
                }

                if (diff != null && diff >= Constants.App.NEW_SESSION_DURATION) {
                    sessionCount++
                    repository.saveSessionCount(sessionCount)
                    _stateFlowSessionCount.value = sessionCount
                }
            }
        }
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        viewModelScope.launch {
            repository.saveCurrentDateTime()
        }
        super.onStop(owner)
    }

    fun deviceSensorChanged(sensorEventValues: FloatArray) {

        val degree = repository.calculateDeviceZAxisRotationDegree(sensorEventValues)

        when {
            degree > Constants.App.ROTATION_LEFT_ANGLE -> {
                _stateFlowTextSize.value = Constants.App.ROTATION_RIGHT_FONT_SIZE
            }
            degree < Constants.App.ROTATION_RIGHT_ANGLE -> {
                _stateFlowTextSize.value = Constants.App.ROTATION_LEFT_FONT_SIZE
            }
            else -> {
                _stateFlowTextSize.value = Constants.App.ROTATION_DEFAULT_FONT_SIZE
            }
        }
    }
}