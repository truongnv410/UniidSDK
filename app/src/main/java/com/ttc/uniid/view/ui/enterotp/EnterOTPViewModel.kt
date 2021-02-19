package com.ttc.uniid.view.ui.enterotp

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.AccuracyRequest
import com.ttc.uniid.data.remote.request.OTPRequest
import com.ttc.uniid.data.remote.request.VerifyOTPSocialRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.view.base.BaseViewModel

class EnterOTPViewModel(app: Application) : BaseViewModel(app) {
    val context = app.applicationContext
    val sendOTPSuccess = SingleLiveData<BaseResponse>()
    val sendOTPAnotherWaySuccess = SingleLiveData<Unit>()
    val accuracyUsernameSuccess = SingleLiveData<BaseResponse>()
    val accuracySocialSuccess = SingleLiveData<BaseResponse>()
    val otpCode = MutableLiveData<String>()
    val invalidOTPCode = MutableLiveData<String>().apply { postValue("") }

    fun resendOTP(otpDestination: String, userInfo: UserInfoResponse, isTryAnotherWay: Boolean) {
        loading.value = true
        userInfo.userInfoDTO?.endUserDTO?.username?.let { us ->
            EnterOTPRepository.getInstance()
                .sendOTPSMS(otpDestination, us) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        if (isTryAnotherWay) {
                            sendOTPAnotherWaySuccess.call()
                        } else {
                            sendOTPSuccess.call()
                        }
                    } else {
                        if(response?.message !=null){
                            toastMessage.value = response.message
                        }else{
                            toastMessage.value = context.getString(R.string.unspecific_error)
                        }
                    }
                }
        }
    }

    fun accuracy(accuracyType: String, userInfo: UserInfoResponse, isTryAnotherWay: Boolean) {
        userInfo.userInfoDTO?.endUserDTO?.username?.let { username ->
            EnterOTPRepository.getInstance()
                .accuracy(
                    AccuracyRequest(
                        accuracyType,
                        username
                    )
                ) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        Toast.makeText(
                            context,
                            context?.getString(R.string.sent_otp_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if(response?.message !=null){
                            toastMessage.value = response.message
                        }else{
                            toastMessage.value = context.getString(R.string.unspecific_error)
                        }
                    }
                }
        }
    }

    private fun isValidOTP(): Boolean {
        var isValid = true
        otpCode.value.isNullOrEmpty().let {
            if (it) {
                invalidOTPCode.value =
                    context.getString(R.string.otp_cannot_be_blank)
                isValid = false
            } else {
                otpCode.value?.length?.compareTo(6).let { check ->
                    check?.let {
                        if (check < 0) {
                            invalidOTPCode.value =
                                context.getString(R.string.minimum_length_otp)
                            isValid = false
                        }
                    }
                }
            }
        }
        return isValid
    }

    fun accuracyUsername(methodSendOTP: String, userInfo: UserInfoResponse) {
        if (!isValidOTP()) {
            return
        }

        loading.value = true
        userInfo.userInfoDTO?.checkerCode?.let { checkerCode ->
            EnterOTPRepository.getInstance()
                .accuracyUsername(
                    OTPRequest(
                        methodSendOTP,
                        checkerCode,
                        otpCode.value ?: ""
                    )
                ) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        accuracyUsernameSuccess.call()
                    } else {
                        if(response?.message !=null){
                            toastMessage.value = response.message
                        }else{
                            toastMessage.value = context.getString(R.string.unspecific_error)
                        }
                    }
                }
        }
    }

    fun sentOTP(userInfo: UserInfoResponse) {
        if (!isValidOTP()) {
            return
        }
        userInfo.userInfoDTO?.endUserDTO?.username?.let { username ->
            loading.value = true
            EnterOTPRepository.getInstance()
                .verifyOTP(
                        otpCode.value ?: "", username
                ) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        accuracyUsernameSuccess.call()
                    } else {
                        if(response?.message !=null){
                            toastMessage.value = response.message
                        }else{
                            toastMessage.value = context.getString(R.string.unspecific_error)
                        }
                    }
                }
        }
    }

    fun accuracySocial(phoneNo: String, socialType: String, username: String) {
        if (!isValidOTP()) {
            return
        }
        loading.value = true
        EnterOTPRepository.getInstance()
            .accuracySocial(
                VerifyOTPSocialRequest(
                    otpCode.value ?: "", phoneNo, socialType, username
                )
            ) { isSuccess, response ->
                loading.value = false
                if (isSuccess) {
                    accuracySocialSuccess.value = response
                } else {
                    if(response?.message !=null){
                        toastMessage.value = response.message
                    }else{
                        toastMessage.value = context.getString(R.string.unspecific_error)
                    }
                }
            }
    }
}