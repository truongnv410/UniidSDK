package com.ttc.uniid.view.ui.createUniIdAccount

import com.ttc.uniid.data.remote.request.RegisterPhonesRequest
import com.ttc.uniid.data.remote.response.RegisterPhonesRespond
import com.ttc.uniid.model.api.ApiClient
import com.ttc.uniid.view.ui.enterotp.EnterOTPRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUniIdAccountRepository {
    fun registerPhonesRequest(
        request: RegisterPhonesRequest,
        onResult: (isSuccess: Boolean, response: RegisterPhonesRespond?) -> Unit
    ) {
        ApiClient.instance.registerPhonesRequest(request).enqueue(object : Callback<RegisterPhonesRespond> {
            override fun onResponse(
                call: Call<RegisterPhonesRespond>?,
                response: Response<RegisterPhonesRespond>?
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

            override fun onFailure(call: Call<RegisterPhonesRespond>?, t: Throwable?) {
                onResult(false, null)
            }
        })
    }
    companion object {
        private var INSTANCE: CreateUniIdAccountRepository? = null

        fun getInstance() = INSTANCE ?: CreateUniIdAccountRepository().also {
            INSTANCE = it
        }
    }
}