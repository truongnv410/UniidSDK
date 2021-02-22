package com.ttc.uniid.view.ui.sendOtp

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.RegisterPhonesRequest
import com.ttc.uniid.data.remote.request.VerifyPhoneRegisterRequest
import com.ttc.uniid.data.remote.response.RegisterPhonesRespond
import com.ttc.uniid.data.remote.response.VerifierPhoneRespond
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.view.base.BaseViewModel
import com.ttc.uniid.view.ui.createUniIdAccount.CreateUniIdAccountRepository


class SendOtpViewModel(app: Application) : BaseViewModel(app) {
    val context = app.applicationContext
    val otpCode = MutableLiveData<String>()
    val invalidOTPCode = MutableLiveData<String>().apply { postValue("") }
    var registerPhonesRespond = MutableLiveData<RegisterPhonesRespond>()
    val verifierPhoneRespond = MutableLiveData<VerifierPhoneRespond>()
    var registerPhonesRequest: RegisterPhonesRequest? = null
    val verifierPhoneSuccess = SingleLiveData<Unit>()
    fun verifyPhoneRegister() {
        if (!isValidOTP()) {
            return
        }
        loading.value = true
        val bodyRequest = VerifyPhoneRegisterRequest(
            registerPhonesRespond?.value?.registerPhonesData?.checkerCode!!,
            otpCode.value.toString(),
            registerPhonesRespond?.value?.registerPhonesData?.phoneNo!!
        )
        SendOtpRepository.getInstance()
            .verifyPhoneRegister(bodyRequest) { isSuccess, response ->
                loading.value = false
                if (isSuccess) {
                    verifierPhoneRespond.value = response
                    loading.value = false
                    verifierPhoneSuccess.call()
                } else {
                    if (response?.message != null) {
                        toastMessage.value = response.message
                    } else {
                        toastMessage.value = context.getString(R.string.unspecific_error)
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

    fun reSendOtp() {
        registerPhonesRespond.value = null
        registerPhonesRequest?.let {
            CreateUniIdAccountRepository.getInstance()
                .registerPhonesRequest(it) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        registerPhonesRespond.value = response
                        loading.value = false

                        Toast.makeText(
                            context,
                            registerPhonesRespond.value?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (response?.message != null) {
                            toastMessage.value = response.message
                        } else {
                            toastMessage.value = context.getString(R.string.unspecific_error)
                        }
                    }
                }

        }
    }
}