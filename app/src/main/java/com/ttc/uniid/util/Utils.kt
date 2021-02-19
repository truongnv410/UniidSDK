package com.ttc.uniid.util

import android.content.Context
import android.text.TextUtils
import com.ttc.uniid.R
import com.ttc.uniid.util.Constants.Companion.EMAIL_REGEX

fun convertStringToMap(query: String?): MutableMap<String, String>? {
    query?.let {
        val keyValuePairs: List<String> =
            query.split("&") //split the string to creat key-value pairs

        val map: MutableMap<String, String> = HashMap()

        for (pair in keyValuePairs)  //iterate over the pairs
        {
            val entry = pair.split("=").toTypedArray() //split the pairs to get key and value
            map[entry[0]] = entry[1] //add them to the hashmap and trim whitespaces
        }
        return map
    }
    return null
}

fun isValidateAccountName(context: Context, account: String): String{
    if(account.isEmpty()){
        return context.getString(R.string.phone_email_username_cannot_be_blank)
    } else if(account.isEmailValid()){
        if(account.length > 64){
            return context.getString(R.string.email_invalid)
        }
    }else if(account.length < 8){
        return context.getString(R.string.username_8_character)
    }else if(account.length > 32){
        return context.getString(R.string.username_32_character)
    }
    return ""
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && EMAIL_REGEX.toPattern().matcher(this).matches()
}