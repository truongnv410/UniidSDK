package com.ttc.uniid.data.remote.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginPasswordRequest(
    @SerializedName("checkerCode") val checkerCode: String,
    @SerializedName("password") val password: String
) : RequestModel()