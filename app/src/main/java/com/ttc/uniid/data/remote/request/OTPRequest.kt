package com.ttc.uniid.data.remote.request

data class OTPRequest(
    val accuracyType: String,
    val checkerCode: String,
    val otp: String

)