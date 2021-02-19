package com.ttc.uniid.view.base


import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivityNew : AppCompatActivity() {
    var setOnBackpressListener: SetOnBackpressListener? = null

    lateinit var viewModelFactory: ViewModelProvider.Factory


    interface SetOnBackpressListener {
        fun onBackPress(): Boolean
    }

    override fun onBackPressed() {
        setOnBackpressListener?.let {
            if (it.onBackPress()) {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }
}