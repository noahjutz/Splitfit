package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.LiveData
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

    fun insert(exercise: Exercise) = viewModelScope.launch { repository.insert(exercise) }
    fun delete(exercise: Exercise) = viewModelScope.launch { repository.delete(exercise) }
}