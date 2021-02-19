package com.ttc.uniid.extension


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import com.ttc.uniid.R


fun AppCompatActivity.startActivity(
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


fun AppCompatActivity.showDialogFragment(
    dialogFragment: DialogFragment,
    TAG: String?,
    addToBackStack: Boolean = false
) {
    val transaction = supportFragmentManager.beginTransaction()
    if (addToBackStack) transaction.addToBackStack(TAG)
    dialogFragment.show(transaction, TAG)
}
fun AppCompatActivity.replaceFragment(
    frg: Fragment,
    bundle: Bundle? = null
) {
    val backStateName: String = frg::class.java.name
    val manager: FragmentManager = supportFragmentManager
    var fragmentPopped = false
    try {
        fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
    } catch (ignored: IllegalStateException) {
    } catch (e: Exception) {
        e.printStackTrace()
    }
    if (!fragmentPopped) { // fragment not in back stack, create it.
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        )
        ft.replace(R.id.container, frg, backStateName)
        ft.addToBackStack(backStateName)
        ft.commitAllowingStateLoss()
    }
}
fun FragmentActivity.replaceFragment(
    frg: Fragment,
    bundle: Bundle? = null
) {
    val backStateName: String = frg::class.java.name
    val manager: FragmentManager = supportFragmentManager
    var fragmentPopped = false
    try {
        fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
    } catch (ignored: IllegalStateException) {
    } catch (e: Exception) {
        e.printStackTrace()
    }
    if (!fragmentPopped) { // fragment not in back stack, create it.
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        )
        frg.arguments = bundle
        ft.replace(R.id.container, frg, backStateName)
        ft.addToBackStack(backStateName)
        ft.commitAllowingStateLoss()
    }
}

fun FragmentActivity.addFragment(
    frg: Fragment,
    bundle: Bundle? = null
) {
    val backStateName: String = frg::class.java.name
    val manager: FragmentManager = supportFragmentManager
    var fragmentPopped = false
    try {
        fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
    } catch (ignored: IllegalStateException) {
    } catch (e: Exception) {
        e.printStackTrace()
    }
    if (!fragmentPopped) { // fragment not in back stack, create it.
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.slide_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.slide_out
        )
        frg.arguments = bundle
        ft.add(R.id.container, frg, backStateName)
        ft.addToBackStack(backStateName)
        ft.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.addFragment(id : Int, fragment : Fragment, bundle: Bundle ?= null){
    fragment?.arguments = bundle
    val fragmentManager: FragmentManager = supportFragmentManager
    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(id, fragment).commit()
}

