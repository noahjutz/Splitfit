package com.noahjutz.gymroutines.di

import com.noahjutz.gymroutines.ui.routines.create.CreateRoutineViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@InstallIn(ActivityComponent::class)
@Module
object ViewModelModule {
    @Provides
    @Named(ROUTINE_ID)
    fun provideRoutineId() = -1 // TODO: Provide routineId at runtime
}