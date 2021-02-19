package com.ttc.uniid.data.remote.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FillerRequest(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String
) : RequestModel()
