package com.noahjutz.splitfit.ui.workout.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.ui.workout.completed.WorkoutCompletedViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WorkoutViewerViewModel(
    private val workoutId: Int,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    sealed class State {
        object Loading : State()
        object Error : State()
        data class Found(val workout: Workout) : State()
    }

    private val _state = MutableStateFlow<State>(State.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val workout = workoutRepository.getWorkout(workoutId)
            _state.value = if (workout != null) State.Found(workout) else State.Error
        }
    }
}
