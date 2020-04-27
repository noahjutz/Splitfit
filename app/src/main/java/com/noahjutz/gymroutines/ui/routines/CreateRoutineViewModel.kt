package com.noahjutz.gymroutines.ui.routines

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    /**
     * Two-way data binding values
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    fun insert(routine: Routine) = viewModelScope.launch { repository.insert(routine) }
    fun update(routine: Routine) = viewModelScope.launch { repository.update(routine) }
    fun getRoutineById(id: Int): Routine? = routines.value?.find { it.routineId == id }

    fun update(routineId: Int) {
        val routine = getRoutineById(routineId)?.apply {
            name = this@CreateRoutineViewModel.name.value.toString()
            description = this@CreateRoutineViewModel.description.value.toString()
        }
        if (routine != null) update(routine)
    }

    fun insert() {
        val routine = Routine(
            name.value.toString(),
            description.value.toString()
        )
        insert(routine)
    }
}