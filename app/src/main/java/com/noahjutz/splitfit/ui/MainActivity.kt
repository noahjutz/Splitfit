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

package com.noahjutz.splitfit.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import com.noahjutz.splitfit.data.AppPrefs
import com.noahjutz.splitfit.data.datastore
import com.noahjutz.splitfit.ui.settings.Theme
import com.noahjutz.splitfit.ui.theme.SplitfitTheme
import kotlinx.coroutines.flow.map
import kotlin.time.ExperimentalTime

val LocalActivity = compositionLocalOf<MainActivity> { error("MainActivity not found") }

class MainActivity : AppCompatActivity() {

    @ExperimentalTime
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appTheme: Theme by applicationContext.datastore.data
                .map { Theme.valueOf(it[AppPrefs.AppTheme.key] ?: Theme.FollowSystem.name) }
                .collectAsState(initial = Theme.FollowSystem)
            SplitfitTheme(colors = appTheme) {
                CompositionLocalProvider(LocalActivity provides this@MainActivity) {
                    SplitfitApp()
                }
            }
        }
    }
}
