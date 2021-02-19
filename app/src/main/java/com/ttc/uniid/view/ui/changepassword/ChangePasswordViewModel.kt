package com.ttc.uniid.view.ui.changepassword

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.ChangePasswordRequest
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.view.base.BaseViewModel

class ChangePasswordViewModel(app: Application) : BaseViewModel(app) {
    val context: Context = app.applicationContext
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val userInfo = MutableLiveData<UserInfoResponse>()
    val invalidPassword = MutableLiveData<String>().apply { postValue("") }
    val invalidCfPassword = MutableLiveData<String>().apply { postValue("") }

    fun changePassword(otpCode: String, info: UserInfoResponse) {
        invalidCfPassword.value = ""
        invalidPassword.value = ""
        var valid = true
        password.value.isNullOrEmpty().let {
            if(it){
                invalidPassword.value =
                    context.getString(R.string.password_cannot_be_blank)
                valid = false
            }
        }
        confirmPassword.value.isNullOrEmpty().let {
            if(it){
                invalidCfPassword.value =
                    context.getString(R.string.confirm_password_cannot_be_blank)
                valid = false
            }
        }
        if (!valid) {
            return
        }
        if(password.value.toString() != confirmPassword.value.toString()){
            invalidCfPassword.value =
                context.getString(R.string.pass_conf_pass_not_same)
            return
        }
        loading.value = true
        info.userInfoDTO?.endUserDTO?.username?.let { username ->
            ChangePasswordRepository.getInstance().changePassword(
                ChangePasswordRequest(
                "DEFAULT",
                otpCode,
                password.value ?: "",
                username
            )
            ){ isSuccess, response ->
                loading.value = false
                if (isSuccess) {
                    userInfo.value = response
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
}