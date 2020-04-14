package com.noahjutz.gymroutines.models

import android.os.Parcel
import android.os.Parcelable

data class UnitDouble(
    override val name: String,
    override var value: Double
) : MeasureUnit, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readDouble()
    )

    override fun toString(): String {
        return "[UnitDouble: $name, $value]"
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeDouble(value)
    }

    override fun describeContents(): kotlin.Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnitDouble> {
        override fun createFromParcel(parcel: Parcel): UnitDouble {
            return UnitDouble(parcel)
        }

        override fun newArray(size: Int): Array<UnitDouble?> {
            return arrayOfNulls(size)
        }
    }
}