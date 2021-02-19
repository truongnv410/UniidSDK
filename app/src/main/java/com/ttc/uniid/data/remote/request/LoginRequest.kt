package com.ttc.uniid.data.remote.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String?
) : RequestModel()