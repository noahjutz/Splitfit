package com.noahjutz.gymroutines.ui.exercises

import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    private val _exercise = MediatorLiveData<Exercise>()
    val exercise: LiveData<Exercise>
        get() = _exercise

    private val _debugText = MediatorLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    init {
        _exercise.addSource(name) { name ->
            _exercise.value = _exercise.value.also {
                it?.name = name.trim()
            }
        }
        _exercise.addSource(description) { description ->
            _exercise.value = _exercise.value.also {
                it?.description = description.trim()
            }
        }

        _debugText.addSource(exercise) { exercise ->
            _debugText.value = "Exercise:\n$exercise"
        }
    }

    /**
     * Initializes [CreateExerciseViewModel] with an [Exercise]
     * @param exerciseId is used to update [_exercise]
     */
    fun init(exerciseId: Int) {
        _exercise.value =
            if (exerciseId != -1) getExerciseById(exerciseId)?.copy() ?: Exercise("")
            else Exercise("")

        name.value = exercise.value?.name
        description.value = exercise.value?.description
    }

    private fun insertOrUpdate(exercise: Exercise) =
        viewModelScope.launch { repository.insert(exercise) }

    fun getExerciseById(id: Int): Exercise? = runBlocking { repository.getExerciseById(id) }

    /**
     * @return true if successful
     */
    fun save(): Boolean {
        if (
            exercise.value?.name.toString().isEmpty()
            || exercise.value?.name.toString().length > 20
            || exercise.value?.description.toString().length > 500
        ) return false

        insertOrUpdate(exercise.value!!)
        return true
    }
}