package com.noahjutz.gymroutines.models

data class Set(
    val fields: ArrayList<Field> = ArrayList(),
    var setType: SetType = SetType.NORMAL
)