package com.noahjutz.gymroutines.ui.routines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noahjutz.gymroutines.data.Repository
import com.noahjutz.gymroutines.data.Routine
import kotlinx.coroutines.launch

@Suppress("unused")
private const val TAG = "RoutinesViewModel"

class RoutinesViewModel(
    private val repository: Repository
) : ViewModel() {
    val routines: LiveData<List<Routine>>
        get() = repository.routines

    private val _debugText = MutableLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    fun updateDebugText() {
        val sb = StringBuilder("Routines:\n")
        routines.value?.forEach { routine ->
            sb.append("$routine\n")
        }
        _debugText.value = sb.toString()
    }

    fun insert(routine: Routine) = viewModelScope.launch { repository.insertOrUpdate(routine) }
    fun delete(routine: Routine) = viewModelScope.launch { repository.delete(routine) }
    fun clearRoutines() = viewModelScope.launch { repository.clearRoutines() }
}