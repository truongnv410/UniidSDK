package com.ttc.uniid.data.remote.request

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterPhonesRequest(
    @SerializedName("clientId")
    val clientId: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("phoneNo")
    val phoneNo: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("username")
    val username: String
) : RequestModel()
