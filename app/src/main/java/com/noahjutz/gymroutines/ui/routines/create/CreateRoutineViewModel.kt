package com.noahjutz.gymroutines.ui.routines.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.*
import com.noahjutz.gymroutines.data.domain.Set
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository,
    private var routineId: Int
) : ViewModel() {
    /**
     * The [RwE] object that is being created/edited
     * @see initRwe
     * @see saveLegacy
     */
    // private val _rwe = MediatorLiveData<RwE>()
    // val rwe: LiveData<RwE>
    //     get() = _rwe

    // TODO: Replace ^ with v DONE
    private val _fullRoutine = MediatorLiveData<FullRoutine>()
    val fullRoutine: LiveData<FullRoutine>
        get() = _fullRoutine

    /**
     * Data binding fields
     * [MediatorLiveData] Sources for [rwe]
     * @see initBinding
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    // private val _exercisesLegacy = MutableLiveData<ArrayList<ExerciseWrapper>>()
    // TODO: Replace ^ with v DONE
    private val _exercises = MutableLiveData<ArrayList<ExerciseImpl>>()

    init {
        // initRwe()
        // TODO: Replace ^ with v DONE
        initFullRoutine()
        initBinding()
    }

    // private fun initBinding() {
    //     name.value = rwe.value!!.routine.name
    //     description.value = rwe.value!!.routine.description
    // }
    // TODO: Replace ^ with v DONE
    private fun initBinding() {
        name.value = fullRoutine.value!!.routine.name
        description.value = fullRoutine.value!!.routine.description
    }

    /**
     * Initializes [RwE] Object
     * Adds [name] and [description] and [_exercisesLegacy] as source
     */
    // private fun initRwe() {
    //     _rwe.run {
    //         value = getRweById(routineId)
    //             ?: RwE(
    //                 Routine(""),
    //                 listOf()
    //             )

    //         _exercisesLegacy.value =
    //             value?.exerciseWrappers as? ArrayList<ExerciseWrapper> ?: ArrayList()

    //         addSource(name) { name ->
    //             _rwe.value = _rwe.value!!.apply {
    //                 routine.name = name.trim()
    //             }
    //         }

    //         addSource(description) { description ->
    //             _rwe.value = _rwe.value!!.apply {
    //                 routine.description = description.trim()
    //             }
    //         }

    //         addSource(_exercisesLegacy) { exercises ->
    //             _rwe.value = RwE(
    //                 _rwe.value!!.routine,
    //                 exercises
    //             )
    //         }
    //     }
    // }

    // TODO: Replace ^ with v DONE
    private fun initFullRoutine() {
        _fullRoutine.run {
            /**
             * Either edit the routine with [routineId] or create a new one.
             */
            value = repository.getFullRoutine(routineId)
                ?: repository.getFullRoutine(
                    repository.insert(
                        FullRoutine(
                            Routine(""),
                            listOf()
                        )
                    ).toInt()
                )!!

            _exercises.value = value?.exercises as ArrayList<ExerciseImpl>

            addSource(name) { name ->
                value = value!!.apply {
                    routine.name = name.trim()
                }
            }

            addSource(description) { description ->
                value = value!!.apply {
                    routine.description = description.trim()
                }
            }

            addSource(_exercises) { exercises ->
                value = FullRoutine(
                    value!!.routine,
                    exercises
                )
            }
        }
    }

    // fun saveLegacy() {
    //     repository.insert(rwe.value!!)
    // }

    // TODO: Replace ^ with v DONE
    fun save() {
        repository.insert(fullRoutine.value!!)
    }

    /**
     * [repository] access functions
     */

    private fun getRweById(rweId: Int): RwE? = runBlocking {
        repository.getRweById(rweId)
    }

    fun getExerciseById(id: Int): Exercise? = runBlocking {
        withContext(IO) {
            repository.getExercise(id)
        }
    }

    fun getSetsById(ewId: Int): List<Set> = runBlocking {
        withContext(IO) {
            repository.getSetsById(ewId)
        }
    }

    // fun addEWs(exerciseWrappers: List<ExerciseWrapper>) {
    //     for (e in exerciseWrappers) addEW(e)
    // }

    // fun removeEW(exerciseWrapper: ExerciseWrapper) {
    //     if (exerciseWrapper in _exercisesLegacy.value!!)
    //         _exercisesLegacy.value = _exercisesLegacy.value!!.apply { remove(exerciseWrapper) }
    //     repository.delete(exerciseWrapper)
    // }

    // fun addEW(exerciseWrapper: ExerciseWrapper) {
    //     val id = repository.insert(exerciseWrapper).toInt()
    //     val ew = repository.getExerciseWrapperById(id)
    //     _exercisesLegacy.value = _exercisesLegacy.value!!.apply { add(ew!!) }
    // }
    // TODO: Replace ^ with v DONE
    fun addExercise(exerciseImpl: ExerciseImpl) {
        _exercises.value = _exercises.value!!.apply { add(exerciseImpl) }
    }

    fun removeExercise(exerciseImpl: ExerciseImpl) {
        _exercises.value = _exercises.value!!.apply { remove(exerciseImpl) }
    }

    /**
     * @param ewId: [ExerciseWrapper] id to assign the set to
     */
    fun addSet(ewId: Int) {
        val s = Set(ewId)
        val setId = repository.insert(s).toInt()
        val set = repository.getSetById(setId)
        Log.d(TAG, "ewId: $ewId, setId: $setId, set: $set")
        // TODO: Display sets in the EW cards, inside of [ExerciseWrapperAdapter]
    }
}
