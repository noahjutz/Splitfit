package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.RwE

@Suppress("unused")
private const val TAG = "RoutinesViewModel"

class RoutinesViewModel(
    private val repository: Repository
) : ViewModel() {

    val routinesWithExercises: LiveData<List<RwE>>
        get() = repository.routinesWithExercises

    fun insert(rwe: RwE) {
        repository.insert(rwe.routine)
    }

    fun delete(rwe: RwE) {
        repository.delete(rwe.routine)
    }
}