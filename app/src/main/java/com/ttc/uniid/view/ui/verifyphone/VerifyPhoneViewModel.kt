package com.ttc.uniid.view.ui.verifyphone

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.GetOTPSocialRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.view.base.BaseViewModel

class VerifyPhoneViewModel(app: Application) : BaseViewModel(app) {
    val context: Context = app.applicationContext
    val sendOTPSuccess = SingleLiveData<BaseResponse>()
    val phone = MutableLiveData<String>()
    val invalidPhone = MutableLiveData<String>().apply { postValue("") }
    private fun isValidPhone(): Boolean {
        var valid = true
        phone.value.isNullOrEmpty().let {
            if (it) {
                invalidPhone.value =
                    context.getString(R.string.phone_cannot_be_blank)
                valid = false
            } else {
                phone.value?.length?.compareTo(8).let { check ->
                    check?.let {
                        if (check < 0) {
                            invalidPhone.value =
                                context.getString(R.string.minimum_length_phone)
                            valid = false
                        }
                    }
                }
            }
        }
        return valid
    }

    fun getOTPSocialFacebook(username: String) {
        if (!isValidPhone()) {
            return
        }
        loading.value = true
        VerifyPhoneRepository.getInstance()
            .requestAccuracySocial(
                GetOTPSocialRequest(
                    phone.value ?: "",
                    username
                )
            ) { isSuccess, response ->
                loading.value = false
                if (isSuccess) {
                    sendOTPSuccess.value = response
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