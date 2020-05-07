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

    fun insert(rwe: RoutineWithExercises) {
        repository.insert(rwe.routine)
    }

    fun delete(routineWithExercises: RoutineWithExercises) {
        repository.delete(routineWithExercises.routine)
    }
}