package com.ttc.uniid.data.remote.response

import com.google.gson.annotations.SerializedName
import com.ttc.uniid.data.remote.request.FindUserNameData
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FindUserNameRespond(
    @SerializedName("data")
    val findUserNameData: FindUserNameData?
) : BaseResponse()