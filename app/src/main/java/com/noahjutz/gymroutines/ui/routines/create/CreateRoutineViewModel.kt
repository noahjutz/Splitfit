package com.noahjutz.gymroutines.ui.routines.create

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

    private val _exercises = MutableLiveData<ArrayList<Exercise>>()

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

            _exercises.value = value?.exercises as? ArrayList<Exercise> ?: ArrayList()

            addSource(name) { name ->
                _rwe.value = _rwe.value!!.apply {
                    routine.name = name.trim()
                }
            }

            addSource(description) { description ->
                _rwe.value = _rwe.value!!.apply {
                    routine.description = description.trim()
                }
            }

            addSource(_exercises) { exercises ->
                _rwe.value = RoutineWithExercises(
                    _rwe.value!!.routine,
                    exercises
                )
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
        if (exercise in _exercises.value!!)
            _exercises.value = _exercises.value!!.apply { remove(exercise) }
    }

    fun addExercise(exercise: Exercise) {
        if (exercise !in _exercises.value!!)
            _exercises.value = _exercises.value!!.apply { add(exercise) }
    }
}
