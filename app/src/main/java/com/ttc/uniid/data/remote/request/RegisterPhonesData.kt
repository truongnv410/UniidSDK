package com.ttc.uniid.data.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterPhonesData(
    @SerializedName("checkerCode")
    val checkerCode: String,
    @SerializedName("phoneNo")
    val phoneNo: String
): Parcelable