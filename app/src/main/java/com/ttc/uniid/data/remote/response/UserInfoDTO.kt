package com.ttc.uniid.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ttc.uniid.data.remote.response.EndUserDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfoDTO(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("count")
    val count: Long,
    @SerializedName("checkerCode")
    var checkerCode: String?,
    @SerializedName("endUserDTO")
    val endUserDTO: EndUserDTO?,
    @SerializedName("useAuthenticator")
    val useAuthenticator: String?
): Parcelable
