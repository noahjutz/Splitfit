package com.noahjutz.splitfit.util

import android.content.Intent
import android.net.Uri
import com.noahjutz.splitfit.ui.MainActivity

fun MainActivity.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
}
