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

import android.content.Intent
import android.net.Uri
import com.noahjutz.splitfit.ui.MainActivity

object IntentsForCompose {
    private lateinit var activity: MainActivity
    fun MainActivity.registerIntentsForCompose() {
        activity = this
    }

    fun openUrl(url: String) {
        activity.startActivity(Intent(Intent.ACTION_VIEW).also { it.data = Uri.parse(url) })
    }
}

object Urls {
    const val googlePlay = "https://play.google.com/store/apps/details?id=com.noahjutz.splitfit"
    const val sourceCode = "https://github.com/noahjutz/Splitfit"
    const val donateLiberapay = "https://liberapay.com/noahjutz/donate"
}
