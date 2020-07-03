package com.noahjutz.gymroutines.ui.exercises.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.Exercise
import kotlinx.coroutines.runBlocking

@Suppress("unused")
private const val TAG = "CreateExerciseViewModel"

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

    init {
        initExercise()
        initBinding()
    }

    private fun initBinding() {
        name.value = exercise.value!!.name
        description.value = exercise.value!!.description
    }

    private fun initExercise() {
        _exercise.run {
            value = getExerciseById(exerciseId)
                ?: Exercise("", "", -1, -1, -1) // TODO

            addSource(name) { nameSource ->
                _exercise.value = _exercise.value!!.apply {
                    name = nameSource.trim()
                }
            }

            addSource(description) { descSource ->
                _exercise.value = _exercise.value!!.apply {
                    description = descSource.trim()
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

    private fun save() {
        insert(exercise.value!!)
        Log.d(TAG, exercise.value.toString())
    }

    /**
     * [repository] access functions
     */

    private fun insert(exercise: Exercise) {
        repository.insert(exercise)
    }

    private fun getExerciseById(id: Int): Exercise? = runBlocking { repository.getExercise(id) }
}
