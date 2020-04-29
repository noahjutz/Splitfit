package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository
) : ViewModel() {
    val routines: LiveData<List<Routine>>
        get() = repository.routines

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>>
        get() = _exercises

    private val allExercises: LiveData<List<Exercise>>
        get() = repository.exercises

    fun debugInsertExercise() {
        val allExercisesList = allExercises.value
        val exercisesList = exercises.value as ArrayList
        if (!allExercisesList.isNullOrEmpty()) {
            val random =
                if (allExercisesList.size == 1) 0
                else Random().nextInt(allExercisesList.size)

            val exercise = allExercisesList[random]

            if (!exercisesList.contains(exercise))
                _exercises.value = exercisesList.apply { add(exercise) }
        }
    }

    fun debugClearExercises() {
        _exercises.value = ArrayList()
    }

    /**
     * Two-way data binding values for EditTexts
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private val _routine = MediatorLiveData<Routine>()
    val routine: LiveData<Routine>
        get() = _routine

    private val _debugText = MediatorLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    init {
        _exercises.value = ArrayList()
        _routine.addSource(name) { name ->
            _routine.value = _routine.value.also {
                it?.name = name.trim()
            }
        }
        _routine.addSource(description) { description ->
            _routine.value = _routine.value.also {
                it?.description = description.trim()
            }
        }

        _debugText.addSource(routine) { routine ->
            _debugText.value =
                "Routine:\n$routine\n\n" +
                        "Exercises:\n${exercises.value}\n\n" +
                        "All exercises:\n${allExercises.value}"
        }

        _debugText.addSource(exercises) { exercises ->
            _debugText.value =
                "Routine:\n${routine.value}\n\n" +
                        "Exercises:\n$exercises\n\n" +
                        "All exercises:\n${allExercises.value}"
        }
    }

    /**
     * Initializes [CreateRoutineViewModel] with a [Routine]
     * @param routineId is used to update [_routine]
     */
    fun init(routineId: Int) {
        _routine.value =
            if (routineId != -1) getRoutineById(routineId)?.copy() ?: Routine("")
            else Routine("")

        name.value = routine.value?.name
        description.value = routine.value?.description
    }

    private fun insertOrUpdate(routine: Routine) =
        viewModelScope.launch { repository.insertOrUpdate(routine) }

    private fun getRoutineById(id: Int): Routine? {
        return runBlocking {
            repository.getRoutineById(id)
        }
    }

    /**
     * @return true if successful
     */
    fun save(): Boolean {
        if (
            routine.value?.name.toString().isEmpty()
            || routine.value?.name.toString().length > 20
            || routine.value?.description.toString().length > 500
        ) return false

        repository.insertExercisesForRoutine(routine.value!!, exercises.value!!)
        insertOrUpdate(routine.value!!)
        return true
    }
}