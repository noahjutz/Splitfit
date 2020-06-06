package com.noahjutz.gymroutines.ui.routines.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.*
import com.noahjutz.gymroutines.data.Set
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
     * @see save
     */
    private val _rwe = MediatorLiveData<RwE>()
    val rwe: LiveData<RwE>
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
     * Initializes [RwE] Object
     * Adds [name] and [description] and [_exercises] as source
     */
    private fun initRwe() {
        _rwe.run {
            value = getRweById(routineId)
                ?: RwE(
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
                _rwe.value = RwE(
                    _rwe.value!!.routine,
                    exercises
                )
            }
        }
    }

    fun save() {
        repository.insert(rwe.value!!)
    }

    /**
     * [repository] access functions
     */

    private fun getRweById(rweId: Int): RwE? = runBlocking {
        repository.getRweById(rweId)
    }

    fun getExerciseById(id: Int): Exercise? = runBlocking {
        withContext(IO) {
            repository.getExerciseById(id)
        }
    }

    fun getSetsById(ewId: Int): List<Set> = runBlocking {
        withContext(IO) {
            repository.getSetsById(ewId)
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

    fun addEW(exerciseWrapper: ExerciseWrapper) {
        val id = repository.insert(exerciseWrapper).toInt()
        val ew = repository.getExerciseWrapperById(id)
        _exercises.value = _exercises.value!!.apply { add(ew!!) }
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
