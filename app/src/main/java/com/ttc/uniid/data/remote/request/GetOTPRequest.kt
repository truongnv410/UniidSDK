package com.ttc.uniid.data.remote.request

data class GetOTPRequest(
    val checkerCode: String,
    val username: String
)
