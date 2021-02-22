package com.ttc.uniid.view.ui.sendOtp

import com.ttc.uniid.data.remote.request.ChangePasswordRequest
import com.ttc.uniid.data.remote.request.VerifyPhoneRegisterRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.VerifierPhoneRespond
import com.ttc.uniid.model.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendOtpRepository {
    fun verifyPhoneRegister(
        request: VerifyPhoneRegisterRequest,
        onResult: (isSuccess: Boolean, response: VerifierPhoneRespond?) -> Unit
    ) {
        ApiClient.instance.verifyPhoneRegister(request)
            .enqueue(object : Callback<VerifierPhoneRespond> {
                override fun onResponse(
                    call: Call<VerifierPhoneRespond>?,
                    response: Response<VerifierPhoneRespond>?
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

                override fun onFailure(call: Call<VerifierPhoneRespond>?, t: Throwable?) {
                    onResult(false, null)
                }
            })
    }
    companion object {
        private var INSTANCE: SendOtpRepository? = null

        fun getInstance() = INSTANCE ?: SendOtpRepository().also {
            INSTANCE = it
        }
    }
}