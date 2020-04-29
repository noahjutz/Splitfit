package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.coroutines.launch

@Suppress("unused")
private const val TAG = "ExercisesViewModel"

class ExercisesViewModel(
    private val repository: Repository
) : ViewModel() {
    val exercises: LiveData<List<Exercise>>
        get() = repository.exercises

    private val _debugText = MutableLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    fun updateDebugText() {
        val sb = StringBuilder("Exercises:\n")
        exercises.value?.forEach { exercise ->
            sb.append("$exercise\n")
        }
        _debugText.value = sb.toString()
    }

    fun insert(exercise: Exercise) = viewModelScope.launch { repository.insert(exercise) }
    fun delete(exercise: Exercise) = viewModelScope.launch { repository.delete(exercise) }
    fun clearExercises() = viewModelScope.launch { repository.clearExercises() }
}