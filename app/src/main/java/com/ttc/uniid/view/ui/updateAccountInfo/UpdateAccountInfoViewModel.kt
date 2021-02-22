package com.ttc.uniid.view.ui.updateAccountInfo

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.FillerRequest
import com.ttc.uniid.data.remote.response.VerifierPhoneRespond
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.util.isEmailValid
import com.ttc.uniid.view.base.BaseViewModel


class UpdateAccountInfoViewModel(app: Application) : BaseViewModel(app) {
    val context = app.applicationContext
    val birthday = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    val invalidEmail = MutableLiveData<String>().apply { postValue("") }
    var verifierPhoneRespond = MutableLiveData<VerifierPhoneRespond>()
    val fillerSuccess = SingleLiveData<Unit>()
    fun filler() {
        if (!email.value.isNullOrEmpty() && !email.value!!.isEmailValid()) {
            invalidEmail.value =
                context.getString(R.string.emailinvalid)
            return
        }
        var bodyRequest = FillerRequest(
            birthday.value ?: "",
            email.value ?: "",
            gender.value ?: ""
        )
        loading.value = true
        UpdateAccountInfoRepository.getInstance().filler(bodyRequest) { isSuccess, response ->
            if (isSuccess) {
                loading.value = false
                Toast.makeText(context, response?.message, Toast.LENGTH_SHORT).show()
                fillerSuccess.call()
            } else {
                if (response?.message != null) {
                    snackMessage.value = response.message
                } else {
                    snackMessage.value = context?.getString(R.string.unspecific_error)
                }
            }

        }
    }
}
