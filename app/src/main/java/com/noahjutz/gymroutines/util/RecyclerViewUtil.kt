/*
 * GymRoutines
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

package com.noahjutz.gymroutines.util

import android.view.View.GONE
import android.widget.TextView

fun TextView.setTextOrHide(input: String) {
    if (input.trim().isEmpty()) visibility = GONE
    else text = input
}

fun TextView.setTextOrUnnamed(input: String) {
    text = if (input.trim().isEmpty()) "Unnamed"
    else input
}