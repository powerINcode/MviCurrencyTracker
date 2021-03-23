package com.example.core.rx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


fun <T> Observable<T>.onMainThread() = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Observable<T>.onIo() = this.subscribeOn(Schedulers.io())

fun <T> Single<T>.onMainThread() = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Single<T>.onIo() = this.subscribeOn(Schedulers.io())

fun <T> Maybe<T>.onMainThread() = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Maybe<T>.onIo() = this.subscribeOn(Schedulers.io())

fun Completable.onMainThread() = this.observeOn(AndroidSchedulers.mainThread())
fun Completable.onIo() = this.subscribeOn(Schedulers.io())

fun <T> Observable<T>.toLiveData(): LiveData<T> {

    return object : MutableLiveData<T>() {
        var disposable: Disposable? = null;

        override fun onActive() {
            super.onActive()

            // Observable -> LiveData
            disposable = this@toLiveData.subscribe {
                this.postValue(it)
            }
        }

        override fun onInactive() {
            disposable?.dispose();
            super.onInactive()
        }
    }
}