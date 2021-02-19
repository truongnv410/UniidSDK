package com.ttc.uniid.view.ui.enterotp

import com.ttc.uniid.data.remote.request.AccuracyRequest
import com.ttc.uniid.data.remote.request.LoginRequest
import com.ttc.uniid.data.remote.request.OTPRequest
import com.ttc.uniid.data.remote.request.VerifyOTPSocialRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.model.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterOTPRepository {

    fun sendOTPSMS(
        otpDestination: String, username: String,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.sendOTP(otpDestination, username)
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>?,
                    response: Response<BaseResponse>?
                ) {
                    if (response != null) {
                        if (response.isSuccessful && response.body()?.errorCode == 100)
                            onResult(true, response.body())
                        else
                            onResult(false, response.body())
                    } else {
                        onResult(false, null)
                    }
                }

                override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                    onResult(false, null)
                }
            })
    }

    fun accuracy(
        request: AccuracyRequest,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.accuracy(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>?,
                response: Response<BaseResponse>?
            ) {
                if (response != null) {
                    if (response.isSuccessful && response.body()?.errorCode == 100)
                        onResult(true, response.body())
                    else
                        onResult(false, response.body())
                } else {
                    onResult(false, null)
                }
            }

            override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                onResult(false, null)
            }
        })
    }

    fun accuracyUsername(
        request: OTPRequest,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.accuracyUser(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>?,
                response: Response<BaseResponse>?
            ) {
                if (response != null) {
                    if (response.isSuccessful && response.body()?.errorCode == 100)
                        onResult(true, response.body())
                    else
                        onResult(false, response.body())
                } else {
                    onResult(false, null)
                }
            }

            override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                onResult(false, null)
            }
        })
    }

    fun verifyOTP(
        otp: String,
        username: String,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.verifyOTP(otp, username).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>?,
                response: Response<BaseResponse>?
            ) {
                if (response != null) {
                    if (response.isSuccessful && response.body()?.errorCode == 100)
                        onResult(true, response.body())
                    else
                        onResult(false, response.body())
                } else {
                    onResult(false, null)
                }
            }

            override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                onResult(false, null)
            }
        })
    }

    fun accuracySocial(
       request: VerifyOTPSocialRequest,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.accuracySocial(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(
                call: Call<BaseResponse>?,
                response: Response<BaseResponse>?
            ) {
                if (response != null) {
                    if (response.isSuccessful && response.body()?.errorCode == 100)
                        onResult(true, response.body())
                    else
                        onResult(false, response.body())
                } else {
                    onResult(false, null)
                }
            }

            override fun onFailure(call: Call<BaseResponse>?, t: Throwable?) {
                onResult(false, null)
            }
        })
    }

    companion object {
        private var INSTANCE: EnterOTPRepository? = null

        fun getInstance() = INSTANCE ?: EnterOTPRepository().also {
            INSTANCE = it
        }
    }
}