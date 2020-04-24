package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import kotlinx.coroutines.launch

private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(private val repository: Repository) : ViewModel() {
    fun insertRoutine(routine: Routine) = viewModelScope.launch { repository.insert(routine) }
}