package com.ttc.uniid.view.ui.updateAccountInfo

import com.ttc.uniid.data.remote.request.FillerRequest
import com.ttc.uniid.data.remote.request.GetOTPRequest
import com.ttc.uniid.data.remote.request.LoginOTPRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.model.api.ApiClient
import com.ttc.uniid.view.ui.login.LoginRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateAccountInfoRepository {
    fun filler(
        fillerRequest: FillerRequest,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.filler(fillerRequest)
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
    companion object {
        private var INSTANCE: UpdateAccountInfoRepository? = null

        fun getInstance() = INSTANCE ?: UpdateAccountInfoRepository().also {
            INSTANCE = it
        }
    }
}