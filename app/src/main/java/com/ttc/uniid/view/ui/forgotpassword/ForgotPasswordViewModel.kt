package com.ttc.uniid.view.ui.forgotpassword

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.LoginRequest
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.util.isValidateAccountName
import com.ttc.uniid.view.base.BaseViewModel

class ForgotPasswordViewModel(app: Application) : BaseViewModel(app) {
    val context = app.applicationContext
    val userName = MutableLiveData<String>()
    val invalidUserName = MutableLiveData<String>().apply { postValue("") }
    val userInfo = MutableLiveData<UserInfoResponse>()

    fun findUserName() {
        invalidUserName.value = ""
        var valid = true
        val errorUsername = isValidateAccountName(context, userName.value ?: "")
        if (errorUsername.isNotEmpty()) {
            invalidUserName.value = errorUsername
            valid = false
        }
        if (!valid) {
            return
        }
        loading.value = true
        ForgotPasswordRepository.getInstance()
            .findUsername(LoginRequest(userName.value ?: "", null)) { isSuccess, response ->
                loading.value = false
                if (isSuccess) {
                    snackMessage.value = "Thành công"
                    userInfo.value = response
                } else {
                    snackMessage.value = context?.getString(R.string.unspecific_error)
                }
            }
    }
}