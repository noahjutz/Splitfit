package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.coroutines.launch

class CreateExerciseViewModel(private val repository: Repository) : ViewModel() {
    fun insert(exercise: Exercise) = viewModelScope.launch { repository.insert(exercise) }

}