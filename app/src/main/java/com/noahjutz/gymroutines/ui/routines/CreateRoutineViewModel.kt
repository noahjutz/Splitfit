package com.noahjutz.gymroutines.ui.routines

import android.util.Log
import androidx.lifecycle.*
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

    private val _routine = MediatorLiveData<Routine>()
    val routine: LiveData<Routine>
        get() = _routine

    init {
        _routine.value = Routine("")

        _routine.addSource(name) { name ->
            _routine.value = _routine.value.also {
                it?.name = name
            }
        }
        _routine.addSource(description) { description ->
            _routine.value = _routine.value.also {
                it?.description = description
            }
        }
    }

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
        if (routine.value != null) insert(routine.value!!)
    }
}