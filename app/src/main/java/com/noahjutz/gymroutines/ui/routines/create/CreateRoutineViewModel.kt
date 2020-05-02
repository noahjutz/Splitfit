package com.noahjutz.gymroutines.ui.routines.create

import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.data.RoutineWithExercises
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _rwe = MediatorLiveData<RoutineWithExercises>()
    val rwe: LiveData<RoutineWithExercises>
        get() = _rwe

    private val _debugText = MediatorLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    init {
        initDebug()
        initRwe()
    }

    override fun onCleared() {
        super.onCleared()
        save()
    }

    private fun initRwe() {
        _rwe.run {
            value = RoutineWithExercises(
                Routine(""),
                listOf()
            )

            addSource(name) { name ->
                _rwe.value = _rwe.value.also {
                    it?.routine?.name = name.trim()
                }
            }
            addSource(description) { description ->
                _rwe.value = _rwe.value.also {
                    it?.routine?.description = description.trim()
                }
            }
        }
    }

    private fun initDebug() {
        _debugText.run {
            addSource(rwe) { routineWithExercises ->
                updateDebugText(routineWithExercises)
            }
        }
    }

    /**
     * Inserts the routine of rwe.
     * Assigns the exerciseIds of rwe.exercises to routines with cross references.
     */
    private fun save() {
        val routine = rwe.value!!.routine
        val routineId = insert(routine).toInt()
        val exerciseIds = rwe.value!!.exercises.map { it.exerciseId }
        assignExercisesToRoutine(routineId, exerciseIds)
    }

    /**
     * Repository access functions
     */
    private fun insert(routine: Routine): Long = runBlocking { repository.insert(routine) }
    private fun getRoutineWithExercisesById(routineId: Int): RoutineWithExercises? =
        runBlocking { repository.getRoutineWithExercisesById(routineId) }

    private fun assignExercisesToRoutine(routineId: Int, exerciseIds: List<Int>) {
        viewModelScope.launch {
            repository.assignExercisesToRoutine(routineId, exerciseIds)
        }
    }

    /**
     * Debug
     */
    private fun updateDebugText(
        routineWithExercises: RoutineWithExercises?
    ) {
        val rwe = routineWithExercises ?: this.rwe.value!!
        val sb = StringBuilder()
        rwe.exercises.forEach { exercise ->
            sb.append("${exercise.name}: ")
                .append("${exercise.description}\n")
        }

        _debugText.value = "RoutineWithExercises:\n" +
                "Name: ${rwe.routine.name}\n" +
                "Desc: ${rwe.routine.description}\n" +
                "ID: ${rwe.routine.routineId}\n\n" +
                "Exercises:\n" +
                "$sb"
    }

    fun debugInsertExercise() {
        // TODO
    }

    fun debugClearExercises() {
        _rwe.value = RoutineWithExercises(
            rwe.value!!.routine,
            listOf()
        )
    }
}