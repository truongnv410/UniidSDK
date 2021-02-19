package com.ttc.uniid.view.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.MyApplication.Companion.instance
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.LoginRequest
import com.ttc.uniid.data.remote.response.BaseResponse
import com.ttc.uniid.data.remote.response.UserInfoResponse
import com.ttc.uniid.model.Item
import com.ttc.uniid.util.Constants.Companion.BASE_URL
import com.ttc.uniid.util.RxUtils
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.util.isValidateAccountName
import com.ttc.uniid.view.base.BaseViewModel
import okhttp3.HttpUrl

class LoginViewModel(app: Application) : BaseViewModel(app) {
    val context = app.applicationContext
    val userName = MutableLiveData<String>()
    val invalidPhone = MutableLiveData<String>().apply { postValue("") }
    val invalidUserName = MutableLiveData<String>().apply { postValue("") }
    val invalidPassword = MutableLiveData<String>().apply { postValue("") }
    val otp = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val invalidOTP = MutableLiveData<String>().apply { postValue("") }
    val password = MutableLiveData<String>()
    val lockPassword = SingleLiveData<Unit>()
    val userInfo = MutableLiveData<UserInfoResponse>()
    val responsePassword = MutableLiveData<UserInfoResponse>()
    val responseOTP = MutableLiveData<BaseResponse>()
    val facebookLink = MutableLiveData<HttpUrl>()
    val ggLink = MutableLiveData<String>()
    val loginOTPResponse = MutableLiveData<UserInfoResponse>()


    fun findUsername(isLoginByPass: Boolean) {
        if (!isLoginByPass && !isValidPhone()) {
            return
        }
        if (isLoginByPass && !isValidUserName()) {
            return
        }
        if (isLoginByPass && !isValidPassword()) {
            return
        }
        loading.value = true
        //Nhận về dữ liệu 2 tham số isSuccess, response để xử lý
        if (isLoginByPass) {
            userName.value?.let { username ->
                LoginRepository.getInstance().findUsername(LoginRequest(username, null)) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        userInfo.value = response
                    } else {
                        if(response?.message != null){
                            toastMessage.value = response.message
                        }else{
                            toastMessage.value = context?.getString(R.string.unspecific_error)
                        }
                    }
                }
            }
        } else {
            phone.value?.let { phone ->
                LoginRepository.getInstance().findUsername(LoginRequest(phone, null)) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        userInfo.value = response
                    } else {
                        if(response?.message != null){
                            toastMessage.value = response.message
                        }else{
                            toastMessage.value = context?.getString(R.string.unspecific_error)
                        }
                    }
                }
            }
        }
    }

    fun requestPasswordLogin(checkerCode: String?) {
        LoginRepository.getInstance().loginByPass(checkerCode ?: "", password.value ?: "") { isSuccess, response ->
            loading.value = false
            if (isSuccess) {
                loading.value = false
                responsePassword.value = response?.body()
            } else {
                if(response?.body() != null){
                    if(129 == response.code()){
                        lockPassword.call()
                    }else{
                        toastMessage.value = response.body()?.message
                    }
                } else{
                    toastMessage.value = context?.getString(R.string.unspecific_error)
                }
            }
        }
    }

    fun requestOTPLogin(checkerCode: String?, username: String?) {
        LoginRepository.getInstance().requestOTPLogin(checkerCode ?: "", username ?: "") { isSuccess, response ->
            loading.value = false
            if (isSuccess) {
                responseOTP.value = response
            } else {
                if(response?.message != null){
                    toastMessage.value = response.message
                }else{
                    toastMessage.value = context?.getString(R.string.unspecific_error)
                }
            }
        }
    }

    private fun isValidUserName(): Boolean {
        invalidUserName.value = ""
        var valid = true
        val errorUsername = isValidateAccountName(context, userName.value ?: "")
        if(errorUsername.isNotEmpty()){
            invalidUserName.value = errorUsername
            valid = false
        }
        return valid
    }

    private fun isValidPassword(): Boolean {
        var valid = true
        password.value.isNullOrEmpty().let {
            if (it) {
                invalidPassword.value =
                    context.getString(R.string.password_cannot_be_blank)
                valid = false
            } else {
                (password.value?.length!! in 33 downTo 7).let { check ->
                    if (check) {
                        invalidPassword.value =
                            context.getString(R.string.length_password)
                        valid = false
                    }
                }
            }
        }
        return valid
    }

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

    private fun isValidOTP(): Boolean {
        var isValid = true
        otp.value.isNullOrEmpty().let {
            if (it) {
                invalidOTP.value =
                    context.getString(R.string.otp_cannot_be_blank)
                isValid = false
            } else {
                otp.value?.length?.compareTo(6).let { check ->
                    check?.let {
                        if (check < 0) {
                            invalidOTP.value =
                                context.getString(R.string.minimum_length_otp)
                            isValid = false
                        }
                    }
                }
            }
        }
        return isValid
    }

    fun loginByOTP(checkerCode: String?) {
        invalidOTP.value = ""
        invalidPhone.value = ""
        if (!isValidPhone() || !isValidOTP()) {
            return
        }
        checkerCode?.let { code ->
            loading.value = true
            LoginRepository.getInstance().loginByOTP(code, otp.value ?: "") { isSuccess, response ->
                loading.value = false
                if (isSuccess) {
                    loginOTPResponse.value = response?.body()
                    loading.value = false
                } else {
                    if(response?.body() != null){
                        if(129 == response.code()){
                            lockPassword.call()
                        }else{
                            toastMessage.value = response.body()?.message
                        }
                    } else{
                        toastMessage.value = context?.getString(R.string.unspecific_error)
                    }
                }
            }
        }
    }

    fun loginFacebook() {
        ggLink.postValue(BASE_URL+"fbs/url")
    }

    fun loginGG() {
        ggLink.postValue(BASE_URL+"gcps/login/consent/sso")
    }
}