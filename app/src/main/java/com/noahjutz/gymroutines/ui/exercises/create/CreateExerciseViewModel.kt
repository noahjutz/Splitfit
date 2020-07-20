package com.noahjutz.gymroutines.ui.exercises.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.coroutines.runBlocking

class CreateExerciseViewModel(
    private val repository: Repository,
    private val exerciseId: Int
) : ViewModel() {
    /**
     * The [Exercise] object that is being created/edited
     * @see initExercise: adds [name] and [description] as source
     * @see save
     */
    private val _exercise = MediatorLiveData<Exercise>()
    val exercise: LiveData<Exercise>
        get() = _exercise

    /**
     * Data binding fields
     * [MediatorLiveData] sources for [exercise]
     * @see initBinding
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val logWeight = MutableLiveData<Boolean>()
    val logReps = MutableLiveData<Boolean>()
    val logTime = MutableLiveData<Boolean>()
    val logDistance = MutableLiveData<Boolean>()

    init {
        initExercise()
        initBinding()
    }

    private fun initBinding() {
        name.value = exercise.value!!.name
        description.value = exercise.value!!.description
        logWeight.value = exercise.value!!.logWeight
        logReps.value = exercise.value!!.logReps
        logTime.value = exercise.value!!.logTime
        logDistance.value = exercise.value!!.logDistance
    }

    private fun initExercise() {
        _exercise.run {
            value = getExerciseById(exerciseId)
                ?: Exercise("", "")

            addSource(name) { source ->
                _exercise.value = _exercise.value!!.apply { name = source.trim() }
            }

            addSource(description) { source ->
                _exercise.value = _exercise.value!!.apply { description = source.trim() }
            }

            addSource(logWeight) { source ->
                _exercise.value = _exercise.value!!.apply { logWeight = source }
            }

            addSource(logReps) { source ->
                _exercise.value = _exercise.value!!.apply { logReps = source }
            }

            addSource(logTime) { source ->
                _exercise.value = _exercise.value!!.apply { logTime = source }
            }

            addSource(logDistance) { source ->
                _exercise.value = _exercise.value!!.apply { logDistance = source }
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

    private fun save() {
        insert(exercise.value!!)
    }

    /**
     * [repository] access functions
     */

    private fun insert(exercise: Exercise) {
        repository.insert(exercise)
    }

    private fun getExerciseById(id: Int): Exercise? = runBlocking { repository.getExercise(id) }
}
