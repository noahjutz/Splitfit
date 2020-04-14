package com.noahjutz.gymroutines.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Set(
    val fieldsJson: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString()
    )

    override fun toString(): String {
        // val r = StringBuilder("[Set: ")
        // val gson = Gson()
        // val type = object: TypeToken<ArrayList<Field>>(){}.type
        // val list: ArrayList<Field> = gson.fromJson(fieldsJson, type)
        // for (f: Field in list) {
        //     r.append(f.toString())
        //         .append("\n")
        // }
        // r.append("]")
        // return r.toString()
        return fieldsJson
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