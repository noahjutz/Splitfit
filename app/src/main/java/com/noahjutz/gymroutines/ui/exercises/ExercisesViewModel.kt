package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ExercisesViewModel(private val repository: Repository) : ViewModel() {
    private val exercises = repository.getExercises()

    val debugText: String
        get() {
            return if (exercises.value?.isEmpty() == true) {
                "Empty List :("
            } else {
                val sb = StringBuilder()
                exercises.value?.forEach { exercise ->
                    sb.append("$exercise\n")
                }
                sb.toString()
            }
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

    fun getExercises(): LiveData<List<Exercise>> {
        return exercises
    }
}