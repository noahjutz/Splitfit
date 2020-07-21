package com.noahjutz.gymroutines.ui.routines

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.domain.FullRoutine

class RoutinesViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {
    val fullRoutines: LiveData<List<FullRoutine>>
        get() = repository.fullRoutines

    fun insert(fullRoutine: FullRoutine) {
        repository.insert(fullRoutine)
    }

    fun delete(fullRoutine: FullRoutine) {
        repository.delete(fullRoutine)
    }
}
