package com.noahjutz.splitfit.ui.workout.viewer

import androidx.lifecycle.ViewModel
import com.noahjutz.splitfit.data.WorkoutRepository
import com.noahjutz.splitfit.data.domain.Workout
import kotlinx.coroutines.runBlocking

class WorkoutViewerViewModel(
    private val workoutId: Int,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    val workout: Workout = runBlocking { workoutRepository.getWorkout(workoutId)!! }
}
