package com.ttc.uniid.view.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val empty = MutableLiveData<Boolean>().apply { value = false }

    val loading = MutableLiveData<Boolean>().apply { value = false }

    val toastMessage = MutableLiveData<String>()

    val snackMessage = MutableLiveData<String>()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}