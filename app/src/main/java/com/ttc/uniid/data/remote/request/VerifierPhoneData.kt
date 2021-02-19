package com.ttc.uniid.data.remote.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class VerifierPhoneData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("useAuthenticator")
    val useAuthenticator: String,
    @SerializedName("endUserDTO")
    val endUserDTO: String,
    @SerializedName("count")
    val count: String,
    @SerializedName("verifierCode")
    val verifierCode: String
): Parcelable