package com.noahjutz.splitfit.util

import android.content.Context
import com.noahjutz.splitfit.data.AppPrefs
import com.noahjutz.splitfit.data.datastore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

suspend fun Context.isFirstRun(): Boolean = withContext(Dispatchers.Default) {
    datastore.data.map { it[AppPrefs.IsFirstRun.key] ?: true }.first()
}
