package com.ttc.uniid.view.ui.createUniIdAccount

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.ttc.uniid.R
import com.ttc.uniid.data.remote.request.RegisterPhonesRequest
import com.ttc.uniid.data.remote.response.RegisterPhonesRespond
import com.ttc.uniid.util.Constants
import com.ttc.uniid.util.SingleLiveData
import com.ttc.uniid.view.base.BaseViewModel

class CreateUniIdAccountViewModel(app: Application) : BaseViewModel(app) {
    val context = app.applicationContext
    val clientId = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val invalidUserName = MutableLiveData<String>().apply { postValue("") }
    val phoneNumber = MutableLiveData<String>()
    val invalidPhoneNumber = MutableLiveData<String>().apply { postValue("") }
    val firstName = MutableLiveData<String>()
    val invalidFirstName = MutableLiveData<String>().apply { postValue("") }
    val lastName = MutableLiveData<String>()
    val invalidLastName = MutableLiveData<String>().apply { postValue("") }
    val passWord = MutableLiveData<String>()
    val invalidPassWord = MutableLiveData<String>().apply { postValue("") }
    val confirmPassword = MutableLiveData<String>()
    val invalidConfirmPassword = MutableLiveData<String>().apply { postValue("") }
    val registerSuccess = SingleLiveData<Unit>()
    val registerPhonesRespond = MutableLiveData<RegisterPhonesRespond>()
    var registerPhonesRequest: RegisterPhonesRequest? = null

    fun clickBtn() {
        registerPhones()
    }


    fun registerPhones() {
        if (!isValidPhone()){
            return
        }
        if (!isValidFirstName()){
            return
        }
        if (!isValidLastname()){
            return
        }
        if (!isValidUserName()){
            return
        }
        if (!isValidPassword()){
            return
        }
        if (!isValidConfirmPassword()){
            return
        }

        loading.value = true
        registerPhonesRequest = RegisterPhonesRequest(
            clientId.value?:"string",
            firstName.value?: "",
            lastName.value?: "",
            passWord.value?:"",
            phoneNumber.value?:"",
            "ACTIVE",
            userName.value?:""
        )
        registerPhonesRequest?.let {
            CreateUniIdAccountRepository.getInstance()
                .registerPhonesRequest(it) { isSuccess, response ->
                    loading.value = false
                    if (isSuccess) {
                        registerPhonesRespond.value = response
                        loading.value = false
                        Toast.makeText(context, registerPhonesRespond.value?.message, Toast.LENGTH_SHORT).show()
                        registerSuccess.call()
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

    private fun isValidFirstName(): Boolean {
        var valid = true
        firstName.value.isNullOrEmpty().let {
            if (it) {
                invalidFirstName.value =
                    context.getString(R.string.firstname_cannot_be_blank)
                valid = false
            } else {
                if (!Constants.PATERN_NAME.toPattern().matcher(firstName.value).matches()){
                    invalidFirstName.value =
                        context.getString(R.string.use_letters_only)
                    valid = false
                }
            }
        }
        return valid
    }
    private fun isValidLastname(): Boolean {
        var valid = true
        lastName.value.isNullOrEmpty().let {
            if (it) {
                invalidLastName.value =
                    context.getString(R.string.lastname_cannot_be_blank)
                valid = false
            } else {
                if (!Constants.PATERN_NAME.toPattern().matcher(lastName.value).matches()){
                    invalidLastName.value =
                        context.getString(R.string.use_letters_only)
                    valid = false
                }
            }
        }
        return valid
    }

    private fun isValidPhone(): Boolean {
        var valid = true
        phoneNumber.value.isNullOrEmpty().let {
            if (it) {
                invalidPhoneNumber.value =
                    context.getString(R.string.phone_cannot_be_blank)
                valid = false
            } else {
                phoneNumber.value?.length?.compareTo(8).let { check ->
                    check?.let {
                        if (check < 0) {
                            invalidPhoneNumber.value =
                                context.getString(R.string.minimum_length_phone)
                            valid = false
                        }
                    }
                }
            }
        }
        return valid
    }

    private fun isValidUserName(): Boolean {
        var valid = true
        userName.value.isNullOrEmpty().let {
            if (it) {
                invalidUserName.value =
                    context.getString(R.string.username_cannot_be_blank)
                valid = false
            } else {
                if (!Constants.PATERN_USER_NAME.toPattern().matcher(userName.value).matches()){
                    invalidUserName.value =
                        context.getString(R.string.use_letters_periods_number_only)
                    valid = false
                }
                userName.value?.length?.compareTo(8).let { check ->
                    check?.let {
                        if (check < 0) {
                            invalidUserName.value =
                                context.getString(R.string.minimum_length_user_name)
                            valid = false
                        }
                    }
                }
            }
        }
        return valid
    }
    private fun isValidConfirmPassword(): Boolean {
        var valid = true
        confirmPassword.value.isNullOrEmpty().let {
            if (it) {
                invalidPassWord.value =
                    context.getString(R.string.confirm_password_cannot_be_blank)
                valid = false
            } else {
                if (confirmPassword.value != passWord.value) {
                    invalidConfirmPassword.value =
                        context.getString(R.string.pass_conf_pass_not_same)
                    valid = false
                }
            }
        }
        return valid
    }
    private fun isValidPassword(): Boolean {
        var valid = true
        passWord.value.isNullOrEmpty().let {
            if (it) {
                invalidPassWord.value =
                    context.getString(R.string.password_cannot_be_blank)
                valid = false
            } else {
                if (!Constants.PATERN_PASSWORD.toPattern().matcher(passWord.value).matches()){
                    valid= false
                    invalidPassWord.value =
                        context.getString(R.string.password_weak)
                }
            }
        }
        return valid
    }
}