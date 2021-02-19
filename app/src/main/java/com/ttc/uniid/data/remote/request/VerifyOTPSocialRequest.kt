package com.ttc.uniid.data.remote.request

data class VerifyOTPSocialRequest(
    val otp: String,
    val phoneNo: String,
    val socialType: String,
    val username: String
)