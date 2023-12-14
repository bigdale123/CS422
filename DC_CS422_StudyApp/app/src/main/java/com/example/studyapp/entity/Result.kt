package com.example.studyapp.entity

import android.os.Parcel
import android.os.Parcelable

data class Result(
    val title: String?,
    val options: Array<String> = Array(4){""},
    val answer: String?,
    var select: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArray() as Array<String>,
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeStringArray(options)
        parcel.writeString(answer)
        parcel.writeString(select)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Result> {
        override fun createFromParcel(parcel: Parcel): Result {
            return Result(parcel)
        }

        override fun newArray(size: Int): Array<Result?> {
            return arrayOfNulls(size)
        }
    }
}