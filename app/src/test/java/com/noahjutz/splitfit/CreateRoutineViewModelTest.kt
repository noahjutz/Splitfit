/*
 * Splitfit
 * Copyright (C) 2020  Noah Jutz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.noahjutz.splitfit

import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.data.domain.Exercise
import com.noahjutz.splitfit.data.domain.Routine
import com.noahjutz.splitfit.data.domain.Set
import com.noahjutz.splitfit.data.domain.SetGroup
import com.noahjutz.splitfit.ui.routines.create.CreateRoutineViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class CreateRoutineViewModelTest {
    // Sample values

    private val sampleName = "Full body"
    private val sampleSetGroups = mutableListOf(
        SetGroup(
            exerciseId = 1,
            sets = mutableListOf(
                Set(12),
                Set(2)
            )
        )
    )
    private val sampleRoutine = Routine(sampleName, sampleSetGroups)

    // Dependencies

    private val repository: Repository = mockk<Repository>(relaxed = true).apply {
        every { getRoutine(1) } returns sampleRoutine
        every { getExercise(1) } returns Exercise("Push up")
    }

    // Tested class

    private val controller = CreateRoutineViewModel(repository, 1)
    private val presenter = controller.Presenter()
    private val editor = controller.Editor()

    // Presenter

    @Test
    fun `Has correct routine name`() {
        Assert.assertEquals(presenter.routine.value.name, sampleName)
    }

    @Test
    fun `Has correct setGroups`() {
        Assert.assertEquals(presenter.routine.value.setGroups, sampleSetGroups)
    }

    @Test
    fun `Has correct Routine`() {
        Assert.assertEquals(sampleRoutine, presenter.routine.value)
    }

    // Editor

    @Test
    fun `Name can be changed`() {
        val newName = "New sample name"
        editor.setName(newName)
        Assert.assertEquals(presenter.routine.value.name, newName)
    }

    @Test
    fun `Can add Exercises (SetGroups)`() {
        val sizeBefore = presenter.routine.value.setGroups.size
        editor.addExercises(listOf(Exercise(), Exercise()))
        val sizeAfter = presenter.routine.value.setGroups.size
        Assert.assertEquals(sizeBefore + 2, sizeAfter)
    }

    @Test
    fun `Can add Set to SetGroup`() {
        val sizeBefore = presenter.routine.value.setGroups.first().sets.size
        editor.addSetTo(sampleSetGroups.first())
        val sizeAfter = presenter.routine.value.setGroups.first().sets.size
        Assert.assertEquals(sizeBefore + 1, sizeAfter)
    }

    @Test
    fun `Can remove Set from SetGroup`() {
        editor.addExercises(listOf(Exercise()))
        val sizeBefore = presenter.routine.value.setGroups.first().sets.size
        editor.deleteSetFrom(presenter.routine.value.setGroups.first(), 1)
        val sizeAfter = presenter.routine.value.setGroups.first().sets.size
        Assert.assertEquals(sizeBefore - 1, sizeAfter)
    }

    @Test
    fun `Removing last Set from SetGroup removes SetGroup`() {
        Assert.assertFalse(
            presenter.routine.value.setGroups.find { it.exerciseId == sampleSetGroups.first().exerciseId } == null
        )
        editor.deleteSetFrom(presenter.routine.value.setGroups.first(), 1)
        editor.deleteSetFrom(presenter.routine.value.setGroups.first(), 0)
        Assert.assertTrue(
            presenter.routine.value.setGroups.find { it.exerciseId == sampleSetGroups.first().exerciseId } == null
        )
    }

    @Test
    fun `Can save`() {
        editor.close()
        verify { repository.insert(presenter.routine.value) }
    }

    @Test
    fun `Set reps, weight, time and duration update properly`() {
        editor.updateSet(
            setGroupIndex = 0,
            setIndex = 0,
            reps = 12,
            weight = 13.0,
            time = 14,
            distance = 15.0
        )
        Assert.assertEquals(
            12,
            presenter.routine.value.setGroups.first().sets.first().reps
        )
        Assert.assertEquals(
            13.0,
            presenter.routine.value.setGroups.first().sets.first().weight
        )
        Assert.assertEquals(
            14,
            presenter.routine.value.setGroups.first().sets.first().time
        )
        Assert.assertEquals(
            15.0,
            presenter.routine.value.setGroups.first().sets.first().distance
        )
    }
}
