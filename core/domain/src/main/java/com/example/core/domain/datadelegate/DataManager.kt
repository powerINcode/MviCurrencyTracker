package com.example.core.domain.datadelegate

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DataManager<Params, T>(private val delegate: DataDelegate<Params, T>) {
    private val subject = BehaviorSubject.create<Data<T>>().apply {
        doOnNext {
            val a = 0
        }
    }

    fun observe(forceReload: Boolean, params: Params): Observable<Data<T>> {
        return delegate.getFromMemory()
            .switchIfEmpty(
                Maybe.defer { delegate.getFromStorage() }
                    .flatMap { update(it).andThen(Maybe.just(it)) }
            )
            .map<Data<T>> { it.asCompleteData() }
            .defaultIfEmpty(Data.Loading())
            .flatMapObservable { data ->
                if (forceReload || data is Data.Loading) {
                    loadAndUpdateCache(params)
                        .onErrorReturn {
                            Data.Error(it, data.content)
                        }
                        .toObservable()
                        .startWithItem(Data.Loading(data.content))
                } else {
                    Observable.just(data)
                }
            }
            .doOnNext(subject::onNext)
            .concatWith(subject.skip(1))
            .subscribeOn(Schedulers.io())
    }

    fun update(data: T): Completable {
        return delegate.putToMemory(data)
            .andThen(delegate.putToStorage(data))
            .doOnComplete {
                subject.onNext(data.asCompleteData())
            }
    }

    private fun loadAndUpdateCache(params: Params): Single<Data<T>> =
        delegate.getFromNetwork(params)
            .flatMap { data ->
                update(data)
                    .andThen(Single.just(data.asCompleteData()))
            }
}