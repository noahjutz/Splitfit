package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ExercisesViewModel(private val repository: Repository) : ViewModel() {
    val exercises: LiveData<List<Exercise>>
        get() = repository.getExercises()

    /**
     * Data Binding fields
     */
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

    fun insertExercise(exercise: Exercise) {
        CoroutineScope(IO).launch {
            repository.insert(exercise)
        }
    }

    fun clearExercises() {
        CoroutineScope(IO).launch {
            repository.clearExercises()
        }
    }
}