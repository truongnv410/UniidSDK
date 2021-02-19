package com.ttc.uniid.data.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FindUserNameData(
    @SerializedName("checkerCode")
    val checkerCode: String

    ): Parcelable