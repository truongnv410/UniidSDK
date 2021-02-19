package com.ttc.uniid.view.ui.recovery

import com.ttc.uniid.data.remote.request.AccuracyRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.model.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecoveryRepository {

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
        accuracyType: String, username: String,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.accuracy(
            AccuracyRequest(
                accuracyType,
                username
            )
        ).enqueue(object : Callback<BaseResponse> {
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
        private var INSTANCE: RecoveryRepository? = null

        fun getInstance() = INSTANCE ?: RecoveryRepository().also {
            INSTANCE = it
        }
    }
}