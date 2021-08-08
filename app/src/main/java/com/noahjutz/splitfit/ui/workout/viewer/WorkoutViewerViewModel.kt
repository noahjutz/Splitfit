package com.noahjutz.splitfit.ui.workout.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.domain.Workout
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutViewerViewModel(
    private val workoutId: Int,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    private val _workout = MutableStateFlow<Workout?>(null)
    val workout = _workout.asStateFlow()

    init {
        viewModelScope.launch {
            _workout.value = workoutRepository.getWorkout(workoutId)
        }
    }
}
