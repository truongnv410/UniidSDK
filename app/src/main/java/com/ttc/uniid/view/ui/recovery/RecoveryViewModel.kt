package com.ttc.uniid.view.ui.recovery

import android.app.Application
import android.content.Context
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.AccuracyRequest
import com.ttc.uniid.data.remote.request.LoginRequest
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.view.base.BaseViewModel
import com.ttc.uniid.view.ui.login.LoginRepository

class RecoveryViewModel(app: Application) : BaseViewModel(app) {
    val context: Context = app.applicationContext
    val sendOTPSuccess = SingleLiveData<Unit>()
    val accuracySuccess = SingleLiveData<Unit>()
    fun accuracy(accuracyType: String, userInfo: UserInfoResponse) {
        userInfo.userInfoDTO?.endUserDTO?.username?.let { username ->
            loading.value = true
            RecoveryRepository.getInstance().accuracy(accuracyType, username) { isSuccess, response ->
                loading.value = false
                if (isSuccess) {
                    accuracySuccess.call()
                } else {
                    snackMessage.value = context.getString(R.string.unspecific_error)
                }
            }
        }
    }

    fun sendOTP(otpDestination: String, userInfo: UserInfoResponse) {
        loading.value = true
        userInfo.userInfoDTO?.endUserDTO?.username?.let { us ->
            RecoveryRepository.getInstance()
                .sendOTPSMS(otpDestination, us) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        sendOTPSuccess.call()
                    } else {
                        snackMessage.value = context.getString(R.string.unspecific_error)
                    }
                }
        }
    }
}