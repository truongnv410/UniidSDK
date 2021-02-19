package com.ttc.uniid.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValueActivity<T : Any>(val fragment: AppCompatActivity) : ReadWriteProperty<AppCompatActivity, T> {
    private var _value: T? = null

    init {
        fragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                _value = null
            }
        })
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        return _value ?: throw IllegalStateException(
                "should never call auto-cleared-value get when it might not be available"
        )
    }

    override fun setValue(thisRef: AppCompatActivity, property: KProperty<*>, value: T) {
        _value = value
    }
}

fun <T : Any> AppCompatActivity.autoCleared() = AutoClearedValueActivity<T>(this)