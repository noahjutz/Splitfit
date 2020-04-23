package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import kotlinx.coroutines.launch

class RoutinesViewModel(
    private val repository: Repository
) : ViewModel() {
    private val routines = repository.getRoutines()

    fun insertRoutine(routine: Routine) {
        viewModelScope.launch {
            repository.insert(routine)
        }
    }

    fun clearRoutines() {
        viewModelScope.launch {
            repository.clearRoutines()
        }
    }

    fun getRoutines(): LiveData<List<Routine>> {
        return routines
    }
}