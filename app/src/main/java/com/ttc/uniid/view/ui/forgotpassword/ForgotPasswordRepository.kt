package com.ttc.uniid.view.ui.forgotpassword

import com.ttc.uniid.data.remote.request.LoginRequest
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.model.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordRepository {

    fun findUsername(
        request: LoginRequest,
        onResult: (isSuccess: Boolean, response: UserInfoResponse?) -> Unit
    ) {
        ApiClient.instance.findUserName(request).enqueue(object : Callback<UserInfoResponse> {
            override fun onResponse(
                call: Call<UserInfoResponse>?,
                response: Response<UserInfoResponse>?
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

            override fun onFailure(call: Call<UserInfoResponse>?, t: Throwable?) {
                onResult(false, null)
            }
        })
    }

    companion object {
        private var INSTANCE: ForgotPasswordRepository? = null

        fun getInstance() = INSTANCE ?: ForgotPasswordRepository().also {
            INSTANCE = it
        }
    }
}