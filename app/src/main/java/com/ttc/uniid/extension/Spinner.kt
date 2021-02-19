package com.unitel.loginsignupsdk.extension

import android.widget.Spinner
import java.lang.reflect.Method

fun Spinner.hideDropDown(){
    try {
        val method: Method = Spinner::class.java.getDeclaredMethod("onDetachedFromWindow")
        method.isAccessible = true
        method.invoke(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}