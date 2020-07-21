package com.noahjutz.gymroutines.ui.exercises

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.coroutines.launch

class ExercisesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    val exercises: LiveData<List<Exercise>>
        get() = repository.exercises

    fun hide(exercise: Exercise, hide: Boolean) =
        viewModelScope.launch { repository.insert(exercise.apply { hidden = hide }) }
}
