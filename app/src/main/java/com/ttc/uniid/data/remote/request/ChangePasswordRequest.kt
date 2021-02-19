package com.ttc.uniid.data.remote.request

data class ChangePasswordRequest(
    val des: String,
    val otp: String,
    val password: String,
    val username: String
)
