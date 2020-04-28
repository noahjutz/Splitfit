package com.noahjutz.gymroutines.ui.routines

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
     * Two-way data binding values for EditTexts
     */
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private val _routine = MediatorLiveData<Routine>()
    val routine: LiveData<Routine>
        get() = _routine

    private val _debugText = MediatorLiveData<String>()
    val debugText: LiveData<String>
        get() = _debugText

    init {
        _routine.addSource(name) { name ->
            _routine.value = _routine.value.also {
                it?.name = name.trim()
            }
        }
        _routine.addSource(description) { description ->
            _routine.value = _routine.value.also {
                it?.description = description.trim()
            }
        }

        _debugText.addSource(routine) { routine ->
            _debugText.value = routine.toString()
        }
    }

    /**
     * Initializes [CreateRoutineViewModel] with a [Routine]
     * @param routineId is used to update [_routine]
     */
    fun init(routineId: Int) {
        _routine.value =
            if (routineId != -1) getRoutineById(routineId)?.copy()
            else Routine("")

        name.value = routine.value?.name
        description.value = routine.value?.description
    }

    private fun insertOrUpdate(routine: Routine) =
        viewModelScope.launch { repository.insertOrUpdate(routine) }

    private fun getRoutineById(id: Int): Routine? = routines.value?.find { it.routineId == id }

    /**
     * @return true if successful
     */
    fun save(): Boolean {
        if (
            routine.value?.name.toString().isEmpty()
            || routine.value?.name.toString().length > 20
            || routine.value?.description.toString().length > 500
        ) return false

        insertOrUpdate(routine.value!!)
        return true
    }
}