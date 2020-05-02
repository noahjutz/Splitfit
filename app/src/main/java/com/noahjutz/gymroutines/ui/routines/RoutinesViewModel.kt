package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.RoutineWithExercises
import kotlinx.coroutines.launch

@Suppress("unused")
private const val TAG = "RoutinesViewModel"

class RoutinesViewModel(
    private val repository: Repository
) : ViewModel() {

    val routinesWithExercises: LiveData<List<RoutineWithExercises>>
        get() = repository.routinesWithExercises

    private val _debugText = MutableLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    fun updateDebugText() {
        val sb = StringBuilder("Routines:\n")
        routinesWithExercises.value?.forEach { rwe ->
            sb.append("$rwe\n")
        }
        _debugText.value = sb.toString()
    }

    fun clearRoutines() = viewModelScope.launch { repository.clearRoutines() }

    fun insert(rwe: RoutineWithExercises) {
        repository.insert(rwe.routine)
        repository.assignExercisesToRoutine(
            rwe.routine.routineId,
            rwe.exercises.map { it.exerciseId })
    }

    fun delete(routineWithExercises: RoutineWithExercises) {
        repository.assignExercisesToRoutine(routineWithExercises.routine.routineId, listOf())
        repository.delete(routineWithExercises.routine)
    }
}