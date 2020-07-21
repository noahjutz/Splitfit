package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.coroutines.launch

// TODO: Constructor injection
class ExercisesViewModel(
    private val repository: Repository
) : ViewModel() {
    val exercises: LiveData<List<Exercise>>
        get() = repository.exercises

    fun hide(exercise: Exercise, hide: Boolean) =
        viewModelScope.launch { repository.insert(exercise.apply { hidden = hide }) }
}
