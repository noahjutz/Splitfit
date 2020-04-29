package com.noahjutz.gymroutines.ui.routines

import android.util.Log
import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.data.RoutineWithExercises
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository
) : ViewModel() {
    private val allExercises: LiveData<List<Exercise>>
        get() = repository.exercises

    private val _routineWithExercises = MediatorLiveData<RoutineWithExercises>()
    val routineWithExercises: LiveData<RoutineWithExercises>
        get() = _routineWithExercises

    private val _debugText = MediatorLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    /**
     * Two-way data binding values for EditTexts
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    init {
        initMediatorLiveData()
    }

    private fun initMediatorLiveData() {
        _routineWithExercises.run {
            addSource(name) { name ->
                _routineWithExercises.value = _routineWithExercises.value.also {
                    it?.routine?.name = name.trim()
                }
            }
            addSource(description) { description ->
                _routineWithExercises.value = _routineWithExercises.value.also {
                    it?.routine?.description = description.trim()
                }
            }
        }

        _debugText.run {
            addSource(routineWithExercises) { routineWithExercises ->
                updateDebugText(routineWithExercises, null)
            }
            addSource(allExercises) { allExercises ->
                updateDebugText(null, allExercises)
            }
        }
    }

    /**
     * @return true if successful
     */
    fun save(): Boolean {
        val routineWithExercises = routineWithExercises.value!!
        val routine = routineWithExercises.routine
        val exercises = routineWithExercises.exercises
        if (
            routine.name.isEmpty()
            || routine.name.length > 20
            || routine.description.length > 500
        ) return false

        insert(routine)
        insertExercisesForRoutine(routine, exercises)
        return true
    }

    /**
     * Initializes [CreateRoutineViewModel] with a [Routine]
     * @param routineId is used to update [_routineWithExercises]
     */
    fun init(routineId: Int) {
        _routineWithExercises.value =
            if (routineId != -1) getRoutineWithExercisesById(routineId)?.copy()
            else RoutineWithExercises(Routine(""), listOf())

        val routine = routineWithExercises.value!!.routine

        name.value = routine.name
        description.value = routine.description
    }


    /**
     * Repository access functions
     */
    private fun insert(routine: Routine) = viewModelScope.launch { repository.insert(routine) }
    private fun insertExercisesForRoutine(routine: Routine, exercises: List<Exercise>) =
        viewModelScope.launch { repository.insertExercisesForRoutine(routine, exercises) }

    private fun getRoutineById(id: Int): Routine? = runBlocking { repository.getRoutineById(id) }
    private fun getRoutineWithExercisesById(routineId: Int): RoutineWithExercises? =
        runBlocking { repository.getRoutineWithExercisesById(routineId) }

    /**
     * Debug
     */
    private fun updateDebugText(
        routineWithExercises: RoutineWithExercises?,
        allExercises: List<Exercise>?
    ) {
        _debugText.value = "RoutineWithExercises:\n${routineWithExercises ?: this.routineWithExercises.value}\n\n" +
                "All Exercises:\n${allExercises ?: this.allExercises.value}"
    }

    fun debugInsertExercise() {
        val allExercisesList = allExercises.value

        if (!allExercisesList.isNullOrEmpty()) {
            val random =
                if (allExercisesList.size == 1) 0
                else Random().nextInt(allExercisesList.size)

            val exercise = allExercisesList[random]

            if (!routineWithExercises.value!!.exercises.contains(exercise)) {
                val newExercisesList =
                    routineWithExercises.value!!.exercises as? ArrayList<Exercise> ?: ArrayList()
                _routineWithExercises.value = RoutineWithExercises(
                    routineWithExercises.value!!.routine,
                    newExercisesList.apply { add(exercise) }
                )
            }
        }
    }

    fun debugClearExercises() {
        // _exercises.value = ArrayList()
        _routineWithExercises.value = RoutineWithExercises(
            routineWithExercises.value!!.routine,
            listOf()
        )
    }
}