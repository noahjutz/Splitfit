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

import com.noahjutz.splitfit.util.NumFormatUtil.formatString
import org.junit.Assert
import org.junit.Test

class NumFormatUtilTest {
    @Test
    fun `Whole number is turned into integer`() {
        Assert.assertEquals("12", 12.0.formatString())
    }

    @Test
    fun `Fraction number is unaltered`() {
        Assert.assertEquals("12.2", 12.2.formatString())
    }

    @Test
    fun `null returns empty string`() {
        Assert.assertEquals("", null.formatString())
    }
}