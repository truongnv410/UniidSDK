package com.ttc.uniid.view.ui.login

import com.ttc.uniid.data.remote.request.GetOTPRequest
import com.ttc.uniid.data.remote.request.LoginOTPRequest
import com.ttc.uniid.data.remote.request.LoginPasswordRequest
import com.ttc.uniid.data.remote.request.LoginRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.model.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

//    fun getRepoList(onResult: (isSuccess: Boolean, response: GitResponse?) -> Unit) {
//
//        ApiClient.instance.getRepo().enqueue(object : Callback<GitResponse> {
//            override fun onResponse(call: Call<GitResponse>?, response: Response<GitResponse>?) {
//                if (response != null && response.isSuccessful)
//                    onResult(true, response.body()!!)
//                else
//                    onResult(false, null)
//            }
//
//            override fun onFailure(call: Call<GitResponse>?, t: Throwable?) {
//                onResult(false, null)
//            }
//
//        })
//    }

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

    fun loginByPass(
        checkerCode: String,
        password: String,
        onResult: (isSuccess: Boolean, response: Response<UserInfoResponse>?) -> Unit
    ) {
        ApiClient.instance.loginByPass(LoginPasswordRequest(checkerCode, password))
            .enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(
                    call: Call<UserInfoResponse>?,
                    response: Response<UserInfoResponse>?
                ) {
                    if (response != null) {
                        if (response.isSuccessful && response.body()?.errorCode == 100)
                            onResult(true, response)
                        else
                            onResult(false, response)
                    } else {
                        onResult(false, null)
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>?, t: Throwable?) {
                    onResult(false, null)
                }
            })
    }

    fun requestOTPLogin(
        checkerCode: String,
        username: String,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.requestOTPLogin(GetOTPRequest(checkerCode, username))
            .enqueue(object : Callback<BaseResponse> {
                override fun onResponse(
                    call: Call<BaseResponse>?,
                    response: Response<BaseResponse>?
                ) {
                    if (response != null) {
                        response.code()
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

    fun loginByOTP(
        checkerCode: String,
        otp: String,
        onResult: (isSuccess: Boolean, response: Response<UserInfoResponse>?) -> Unit
    ) {
        ApiClient.instance.loginByOTP(LoginOTPRequest(checkerCode, otp))
            .enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(
                    call: Call<UserInfoResponse>?,
                    response: Response<UserInfoResponse>?
                ) {
                    if (response != null) {
                        response.code()
                        if (response.isSuccessful && response.body()?.errorCode == 100)
                            onResult(true, response)
                        else
                            onResult(false, response)
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
        private var INSTANCE: LoginRepository? = null

        fun getInstance() = INSTANCE ?: LoginRepository().also {
            INSTANCE = it
        }
    }
}