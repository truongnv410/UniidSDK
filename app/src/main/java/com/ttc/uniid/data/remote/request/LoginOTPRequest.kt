package com.ttc.uniid.data.remote.request

data class LoginOTPRequest(val checkerCode: String, val otp: String)