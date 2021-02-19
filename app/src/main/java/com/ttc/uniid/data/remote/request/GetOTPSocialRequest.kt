package com.ttc.uniid.data.remote.request

data class GetOTPSocialRequest(
    val phoneNo: String,
    val username: String
)
