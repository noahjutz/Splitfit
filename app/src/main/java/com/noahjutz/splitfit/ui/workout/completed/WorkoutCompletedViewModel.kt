package com.noahjutz.splitfit.ui.workout.completed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.splitfit.data.RoutineRepository
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.domain.Routine
import com.noahjutz.splitfit.data.domain.Workout
import com.noahjutz.splitfit.data.domain.toRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutCompletedViewModel(
    routineId: Int,
    workoutId: Int,
    private val workoutRepository: WorkoutRepository,
    private val routineRepository: RoutineRepository,
) : ViewModel() {
    sealed class State {
        object Loading : State()
        object Error : State()
        data class Found(val routine: Routine, val workout: Workout) : State()
    }

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val routine = routineRepository.getRoutine(routineId)
            val workout = workoutRepository.getWorkout(workoutId)
            _state.value =
                if (routine == null || workout == null) State.Error
                else State.Found(routine, workout)
        }
    }

    fun updateRoutine() {
        (state.value as? State.Found)?.let { state ->
            val newRoutine = state.workout.toRoutine(state.routine.routineId)
            routineRepository.insert(newRoutine)
        }
    }
}