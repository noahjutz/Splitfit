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

package com.noahjutz.splitfit.di

import androidx.room.Room
import com.noahjutz.splitfit.data.AppDatabase
import com.noahjutz.splitfit.data.Repository
import com.noahjutz.splitfit.data.dao.ExerciseDao
import com.noahjutz.splitfit.data.dao.RoutineDao
import com.noahjutz.splitfit.ui.exercises.ExercisesViewModel
import com.noahjutz.splitfit.ui.routines.RoutinesViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.PickExerciseViewModel
import com.noahjutz.splitfit.ui.routines.create.pick.SharedExerciseViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    single<AppDatabase> {
        Room
            .databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "workout_routines_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    factory<ExerciseDao> {
        get<AppDatabase>().exerciseDao
    }

    factory<RoutineDao> {
        get<AppDatabase>().routineDao
    }

    factory<Repository> {
        Repository(get<ExerciseDao>(), get<RoutineDao>())
    }

    viewModel {
        SharedExerciseViewModel()
    }

    viewModel {
        RoutinesViewModel(get())
    }

    viewModel {
        ExercisesViewModel(get())
    }

    viewModel {
        PickExerciseViewModel(get())
    }
}
