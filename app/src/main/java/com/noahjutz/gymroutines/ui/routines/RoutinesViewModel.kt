package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Rwe

@Suppress("unused")
private const val TAG = "RoutinesViewModel"

class RoutinesViewModel(
    private val repository: Repository
) : ViewModel() {

    val routinesWithExercises: LiveData<List<Rwe>>
        get() = repository.routinesWithExercises

    fun insert(rwe: Rwe) {
        repository.insert(rwe.routine)
    }

    fun delete(rwe: Rwe) {
        repository.delete(rwe.routine)
    }
}