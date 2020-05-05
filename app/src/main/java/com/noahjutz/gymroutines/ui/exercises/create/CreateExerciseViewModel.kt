package com.noahjutz.gymroutines.ui.exercises.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Exercise
import com.noahjutz.gymroutines.data.Repository
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
                ?: Exercise("")

            addSource(name) { name ->
                _exercise.value = _exercise.value.also {
                    it?.name = name.trim()
                }
            }
            addSource(description) { description ->
                _exercise.value = _exercise.value.also {
                    it?.description = description.trim()
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
        Log.d(TAG, "exercises: ${repository.exercises.value} exercise: ${exercise.value}")
    }

    /**
     * [repository] access functions
     */

    private fun insert(exercise: Exercise) {
        repository.insert(exercise)
    }

    fun getExerciseById(id: Int): Exercise? = runBlocking { repository.getExerciseById(id) }
}