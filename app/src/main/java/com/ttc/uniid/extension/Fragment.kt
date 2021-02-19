package com.ttc.uniid.extension

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ttc.uniid.customview.CustomAlertDialog

fun Fragment.showSoftKeyboard(windowToken: IBinder?, show: Boolean) {
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    when (show) {
        true -> imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        else -> imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Fragment.showDialogFragment(
    dialogFragment: DialogFragment,
    TAG: String?,
    addToBackStack: Boolean = false
) {
    val transaction = activity?.supportFragmentManager?.beginTransaction()
    if (addToBackStack) transaction?.addToBackStack(TAG)
    if (transaction != null) {
        dialogFragment.show(transaction, TAG)
    }
}

fun Fragment.showDialogResult(
    title: String? = null,
    @DrawableRes icon: Int? = null,
    message: Pair<String, Int>? = null,
    cancelable: Boolean? = false,
    actionOneMessage: String? = null,
    actionOneAction: (() -> Unit)? = null
) {
    val dialog = CustomAlertDialog.Builder(
        title,
        icon,
        message,
        cancelable,
        actionOneMessage,
        actionOneAction
    ).build()
    showDialogFragment(dialog, dialog.tag, false)
}

fun FragmentActivity.startActivity(
    cls: Class<*>,
    bundle: Bundle? = null,
    clearTop: Boolean = false
) {
    val intent = Intent(this, cls).apply {
        if (clearTop) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        bundle?.let {
            putExtras(it)
        }
    }
    startActivity(intent)
}