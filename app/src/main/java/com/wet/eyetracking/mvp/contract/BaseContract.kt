package com.wet.eyetracking.mvp.contract

import io.reactivex.disposables.Disposable

class BaseContract {

    interface View

    interface Presenter<in V> {
        fun subscribe(subscription: Disposable)
        fun unsubscribe()
        fun attach(view: V)
        fun detach()
    }
}