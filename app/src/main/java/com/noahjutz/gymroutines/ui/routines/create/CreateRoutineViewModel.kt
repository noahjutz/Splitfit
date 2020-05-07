package com.noahjutz.gymroutines.ui.routines.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import com.noahjutz.gymroutines.data.RoutineWithExercises
import kotlinx.coroutines.runBlocking

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository,
    private var routineId: Int
) : ViewModel() {
    /**
     * The [RoutineWithExercises] object that is being created/edited
     * @see initRwe
     * @see save
     */
    private val _rwe = MediatorLiveData<RoutineWithExercises>()
    val rwe: LiveData<RoutineWithExercises>
        get() = _rwe

    /**
     * Data binding fields
     * [MediatorLiveData] Sources for [rwe]
     * @see initBinding
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    init {
        initRwe()
        initBinding()
    }

    private fun initBinding() {
        name.value = rwe.value!!.routine.name
        description.value = rwe.value!!.routine.description
    }

    /**
     * Initializes [RoutineWithExercises] Object
     * Adds [name] and [description] as source
     */
    private fun initRwe() {
        _rwe.run {
            value = getRweById(routineId)
                ?: RoutineWithExercises(
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

    /**
     * Auto-save
     */
    override fun onCleared() {
        super.onCleared()
        save()
    }

    /**
     * Inserts the [rwe]'s [Routine].
     * @see insert
     * Assigns the [rwe]'s [Exercise] list to routines with cross references.
     * @see assignExercisesToRoutine
     */
    private fun save() {
        val routine = rwe.value!!.routine
        val routineId = insert(routine).toInt()
        val exerciseIds = rwe.value!!.exercises.map { it.exerciseId }
        assignExercisesToRoutine(routineId, exerciseIds)
    }

    /**
     * [repository] access functions
     */
    private fun insert(routine: Routine): Long = runBlocking {
        repository.insert(routine)
    }

    private fun getRweById(routineId: Int): RoutineWithExercises? = runBlocking {
        repository.getRweById(routineId)
    }

    private fun assignExercisesToRoutine(routineId: Int, exerciseIds: List<Int>) = runBlocking {
        repository.assignExercisesToRoutine(routineId, exerciseIds)
    }

    private fun unassignExercisesFromRoutine(routineId: Int) = runBlocking {
        repository.unassignExercisesFromRoutine(routineId)
    }

    /**
     * Functions for [CreateRoutineFragment]
     */
    fun addExercises(exercises: List<Exercise>) {
        for (e in exercises) addExercise(e)
    }

    fun removeExercise(exercise: Exercise) {
        val exercisesList = rwe.value?.exercises as? ArrayList ?: ArrayList()
        if (exercise in exercisesList) exercisesList.remove(exercise)
        _rwe.value = RoutineWithExercises(
            rwe.value!!.routine,
            exercisesList
        )
    }

    fun addExercise(exercise: Exercise) {
        val exercisesList = rwe.value?.exercises as? ArrayList ?: ArrayList()
        if (exercise !in exercisesList) exercisesList.add(exercise)
        _rwe.value = RoutineWithExercises(
            rwe.value!!.routine,
            exercisesList
        )
    }
}
