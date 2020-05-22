package com.noahjutz.gymroutines.ui.routines.create

import android.util.Log
import androidx.lifecycle.*
import com.noahjutz.gymroutines.data.*
import com.noahjutz.gymroutines.data.dao.ExerciseWrapperDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository,
    private var routineId: Int
) : ViewModel() {
    /**
     * The [Rwe] object that is being created/edited
     * @see initRwe
     * @see save
     */
    private val _rwe = MediatorLiveData<Rwe>()
    val rwe: LiveData<Rwe>
        get() = _rwe

    /**
     * Data binding fields
     * [MediatorLiveData] Sources for [rwe]
     * @see initBinding
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private val _exercises = MutableLiveData<ArrayList<ExerciseWrapper>>()

    init {
        initRwe()
        initBinding()
    }

    private fun initBinding() {
        name.value = rwe.value!!.routine.name
        description.value = rwe.value!!.routine.description
    }

    /**
     * Initializes [Rwe] Object
     * Adds [name] and [description] as source
     */
    private fun initRwe() {
        _rwe.run {
            value = getRweById(routineId)
                ?: Rwe(
                    Routine(""),
                    listOf()
                )

            _exercises.value = value?.exerciseWrappers as? ArrayList<ExerciseWrapper> ?: ArrayList()

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
                _rwe.value = Rwe(
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
     * Inserts the [rwe]'s [ExerciseWrapper] list to the [AppDatabase] using [ExerciseWrapperDao]
     * Assigns the [rwe]'s [ExerciseWrapper] list to routines with cross references.
     * @see assign
     */
    private fun save() {
        val routine = rwe.value!!.routine
        val routineId = insert(routine).toInt()
        val exerciseWrappers = rwe.value!!.exerciseWrappers
        assign(routineId, exerciseWrappers)
    }

    /**
     * [repository] access functions
     */

    private fun insert(routine: Routine): Long = runBlocking {
        repository.insert(routine)
    }

    private fun insert(exerciseWrapper: ExerciseWrapper): Long = runBlocking {
        repository.insert(exerciseWrapper)
    }

    private fun getRweById(rweId: Int): Rwe? = runBlocking {
        repository.getRweById(rweId)
    }

    fun getExerciseById(id: Int): Exercise? = runBlocking {
        withContext(Dispatchers.IO) {
            repository.getExerciseById(id)
        }
    }

    /**
     * Inserts the [ExerciseWrapper]s and creates [RoutineExerciseCrossRef]s
     * @param routineId is used for the [RoutineExerciseCrossRef]s
     * @param exerciseWrappers are inserted and then assigned to [RoutineExerciseCrossRef]s
     */
    private fun assign(
        routineId: Int,
        exerciseWrappers: List<ExerciseWrapper>
    ) = runBlocking {
        val exerciseWrapperIds = ArrayList<Int>()
        for (e in exerciseWrappers) {
            exerciseWrapperIds.add(insert(e).toInt())
        }
        repository.assignExercisesToRoutine(routineId, exerciseWrapperIds)
    }

    private fun assign(routineId: Int, exerciseWrapperId: Int) {
        viewModelScope.launch {
            repository.assignEW(routineId, exerciseWrapperId)
        }
    }

    fun addEWs(exerciseWrappers: List<ExerciseWrapper>) {
        for (e in exerciseWrappers) addEW(e)
    }

    fun removeEW(exerciseWrapper: ExerciseWrapper) {
        if (exerciseWrapper in _exercises.value!!)
            _exercises.value = _exercises.value!!.apply { remove(exerciseWrapper) }
        repository.delete(exerciseWrapper)
    }

    fun addEW(exerciseWrapper: ExerciseWrapper): Int {
        _exercises.value = _exercises.value!!.apply { add(exerciseWrapper) }
        return repository.insert(exerciseWrapper).toInt()
    }

    /**
     * @param id: [ExerciseWrapper] id to assign the set to
     */
    fun addSet(id: Int) {
        Log.d(TAG, "Clicked! $id")
    }
}
