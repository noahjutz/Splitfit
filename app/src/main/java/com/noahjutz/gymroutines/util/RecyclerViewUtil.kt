package com.noahjutz.gymroutines.util

import android.view.View.GONE
import android.widget.TextView

fun TextView.setTextOrHide(input: String) {
    if (input.trim().isEmpty()) visibility = GONE
    else text = input
}
