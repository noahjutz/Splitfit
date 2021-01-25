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

package com.noahjutz.splitfit.util

import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Suppress("ObjectPropertyName")
object RestTimer {
    private var countDownTimer: CountDownTimer? = null

    private val _remainingMillis = MutableStateFlow(0L)
    val remainingMillis = _remainingMillis.asStateFlow()

    /**
     * Start a new timer, cancelling the old one
     */
    fun start(millis: Long) {
        cancel()
        countDownTimer = object : CountDownTimer(millis, 1) {
            override fun onTick(millis: Long) {
                _remainingMillis.value = millis
            }

            override fun onFinish() {
                _remainingMillis.value = 0L
            }
        }.also { it.start() }
    }

    /**
     * Cancel the current timer
     */
    fun cancel() {
        _remainingMillis.value = 0L
        countDownTimer?.cancel()
        countDownTimer = null
    }
}