package com.noahjutz.gymroutines.models

import android.os.Parcel
import android.os.Parcelable

data class Set(
    val fieldsJson: String,
    val type: Int // Warm up, etc
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fieldsJson)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Set> {
        override fun createFromParcel(parcel: Parcel): Set {
            return Set(parcel)
        }

        override fun newArray(size: Int): Array<Set?> {
            return arrayOfNulls(size)
        }
    }
}