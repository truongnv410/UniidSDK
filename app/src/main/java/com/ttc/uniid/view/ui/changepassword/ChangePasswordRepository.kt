package com.ttc.uniid.view.ui.changepassword

import com.ttc.uniid.data.remote.request.ChangePasswordRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.model.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordRepository {

    fun changePassword(
        request: ChangePasswordRequest,
        onResult: (isSuccess: Boolean, response: UserInfoResponse?) -> Unit
    ) {
        ApiClient.instance.changePassword(request)
            .enqueue(object : Callback<UserInfoResponse> {
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
        private var INSTANCE: ChangePasswordRepository? = null

        fun getInstance() = INSTANCE ?: ChangePasswordRepository().also {
            INSTANCE = it
        }
    }
}