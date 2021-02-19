package com.ttc.uniid.util

import com.ttc.uniid.view.base.BaseViewModel
import io.reactivex.disposables.Disposable

fun Disposable.add(viewModel: BaseViewModel) {
    viewModel.addDisposable(this)
}