package com.ttc.uniid.data.remote.response

import com.google.gson.annotations.SerializedName
import com.ttc.uniid.data.remote.request.VerifierPhoneData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VerifierPhoneRespond(
    @SerializedName("data")
    val verifierPhoneData: VerifierPhoneData?
) : BaseResponse()