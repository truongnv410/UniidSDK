package com.ttc.uniid.data.remote.request

import com.google.gson.annotations.SerializedName
import com.ttc.uniid.data.remote.request.RequestModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VerifyPhoneRegisterRequest(
    @SerializedName("checkerCode")
    val checkerCode: String,
    @SerializedName("otp")
    val otp: String,
    @SerializedName("phoneNo")
    val phoneNo: String

) : RequestModel()