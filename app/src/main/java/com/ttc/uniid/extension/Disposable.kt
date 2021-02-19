package com.ttc.uniid.extension

import com.ttc.uniid.view.base.BaseViewModel
import io.reactivex.disposables.Disposable

fun Disposable.add(viewModel: BaseViewModel) {
    viewModel.addDisposable(this)
}