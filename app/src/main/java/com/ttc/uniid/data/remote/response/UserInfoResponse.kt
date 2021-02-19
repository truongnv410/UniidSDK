package com.ttc.uniid.data.remote.response

import com.google.gson.annotations.SerializedName
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.UserInfoDTO
import kotlinx.android.parcel.Parcelize
@Parcelize
data class UserInfoResponse(
    @SerializedName("data")
    val userInfoDTO: UserInfoDTO?
): BaseResponse()