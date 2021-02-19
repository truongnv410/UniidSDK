package com.unitel.loginsignupsdk.extension

import android.annotation.SuppressLint
import com.ttc.uniid.util.VNCharacterUtils
import java.text.SimpleDateFormat
import java.util.*

fun String.Companion.empty() = ""

@SuppressLint("SimpleDateFormat")
fun String.convertStringDate(fromFormat: String, toFormat: String): String? {
    val date: Date?
    val from = SimpleDateFormat(fromFormat)
    val to = SimpleDateFormat(toFormat)
    try {
        date = from.parse(this)
        return date?.let {
            to.format(date)
        }
    } catch (e: Exception) {
        return null
    }
}

fun String.removeAccent(): String {
    return VNCharacterUtils.removeAccent(this)
}

fun CharSequence.isEmptyOrNull():Boolean{
    return this.trim().isEmpty() || this == "null"
}

val regexPassword = "^{6,20}\$".toRegex()
val regexSpecialCharacter = "^[a-zA-Z0-9]{1,50}\$".toRegex()
fun String.isValidUsername(): Boolean{
    return regexSpecialCharacter.matches(this)
}
fun String.isValidPassword(): Boolean{
    return regexPassword.matches(this)
}

