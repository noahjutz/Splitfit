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

object RegexPatterns {
    val integer = """
        ^$|^0$|^[1-9]\d{0,3}$
    """.trimIndent().toRegex()
    val float = """
        ^(0|[1-9]\d{0,2})?((?<=\d)\.)?((?<=\.)\d{1,3}$)?
    """.trimIndent().toRegex()
    const val time = "" // TODO
}
