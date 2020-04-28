package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.coroutines.launch

@Suppress("unused")
private const val TAG = "CreateExerciseViewModel"

class CreateExerciseViewModel(
    private val repository: Repository
) : ViewModel() {
    val exercises: LiveData<List<Exercise>>
        get() = repository.exercises

    /**
     * Two-way data binding values
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    fun insert(exercise: Exercise) = viewModelScope.launch { repository.insertOrUpdate(exercise) }
    fun update(exercise: Exercise) = viewModelScope.launch { repository.update(exercise) }
    fun getExerciseById(id: Int): Exercise? = exercises.value?.find { it.exerciseId == id }

    fun update(exerciseId: Int) {
        val exercise = getExerciseById(exerciseId)?.apply {
            name = this@CreateExerciseViewModel.name.value.toString()
            description = this@CreateExerciseViewModel.description.value.toString()
        }
        if (exercise != null) update(exercise)
    }

    fun insert() {
        val exercise = Exercise(
            name.value.toString(),
            description.value.toString()
        )
        insert(exercise)
    }
}