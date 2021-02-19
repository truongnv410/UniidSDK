package com.ttc.uniid.view.ui.verifyphone

import com.ttc.uniid.data.remote.request.GetOTPSocialRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.model.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyPhoneRepository {

    fun requestAccuracySocial(
        request: GetOTPSocialRequest,
        onResult: (isSuccess: Boolean, response: BaseResponse?) -> Unit
    ) {
        ApiClient.instance.requestAccuracySocial(request)
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

    companion object {
        private var INSTANCE: VerifyPhoneRepository? = null

        fun getInstance() = INSTANCE ?: VerifyPhoneRepository().also {
            INSTANCE = it
        }
    }
}