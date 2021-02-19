package com.ttc.uniid.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BaseResponse(
    @IgnoredOnParcel
    @SerializedName("status")
    val errorCode: Int = 0,

    @IgnoredOnParcel
    @SerializedName("error")
    var message: String? = null
) : Parcelable