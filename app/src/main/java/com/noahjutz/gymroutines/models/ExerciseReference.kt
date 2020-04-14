package com.noahjutz.gymroutines.models

import android.os.Parcel
import android.os.Parcelable

data class ExerciseReference(
    var setsJson: String,
    val idToRef: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(setsJson)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExerciseReference> {
        override fun createFromParcel(parcel: Parcel): ExerciseReference {
            return ExerciseReference(parcel)
        }

        override fun newArray(size: Int): Array<ExerciseReference?> {
            return arrayOfNulls(size)
        }
    }
}