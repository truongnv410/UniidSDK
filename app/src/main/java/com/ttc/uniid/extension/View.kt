package com.ttc.uniid.extension

import android.view.View

fun View.setVisible(visible: Boolean, invisible: Boolean? = false) {
    visibility = when {
        visible -> View.VISIBLE
        invisible == true -> View.INVISIBLE
        else -> View.GONE
    }
}