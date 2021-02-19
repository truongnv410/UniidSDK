package com.ttc.uniid.data.remote.response

import com.google.gson.annotations.SerializedName
import com.ttc.uniid.data.remote.request.RegisterPhonesData
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RegisterPhonesRespond(
    @SerializedName("data")
    val registerPhonesData: RegisterPhonesData?
) : BaseResponse()