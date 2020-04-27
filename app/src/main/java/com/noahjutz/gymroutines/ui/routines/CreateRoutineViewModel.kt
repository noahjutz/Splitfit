package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import kotlinx.coroutines.launch

@Suppress("unused")
private const val TAG = "CreateRoutineViewModel"

class CreateRoutineViewModel(
    private val repository: Repository
) : ViewModel() {
    val routines: LiveData<List<Routine>>
        get() = repository.routines

    fun insert(routine: Routine) = viewModelScope.launch { repository.insert(routine) }
    fun update(routine: Routine) = viewModelScope.launch { repository.update(routine) }
    fun getRoutineById(id: Int): Routine? = routines.value?.find { it.routineId == id }
}