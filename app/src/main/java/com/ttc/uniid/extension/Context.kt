package com.ttc.uniid.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


fun Context.showDialog(
    title: String? = null,
    message: String? = null,
    cancelable: Boolean? = false,
    positiveMessage: String? = null,
    positiveAction: (() -> Unit)? = null,
    negativeMessage: String? = null,
    negativeAction: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        title?.let { setTitle(it) }
        cancelable?.let { setCancelable(it) }
        message?.let { setMessage(message) }
        positiveMessage?.let { setPositiveButton(it) { _, _ -> positiveAction?.invoke() } }
        negativeMessage?.let { setNegativeButton(it) { _, _ -> negativeAction?.invoke() } }
    }.create().show()
}

fun Context.showSoftKeyboard(show: Boolean) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    when (show) {
        true -> imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        else -> when (this) {
            is Activity -> imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            is Fragment -> imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        }
    }
}

fun Context.dpToPx(valueInDp: Float): Float {
    val metrics = this.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

fun Context.openWeb(url: String){
    val browserIntent =  Intent(Intent.ACTION_VIEW, Uri.parse(url))
    this.startActivity(browserIntent)
}