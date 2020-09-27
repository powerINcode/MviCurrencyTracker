package com.example.core.usecase

import com.example.core_data.datadelegate.Data
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface UseCase<Params, Result> {
    operator fun invoke(params: Params): Result
}

interface SingleUseCase<Params, Result>: UseCase<Params, Single<Result>>

interface SingleSimpleUseCase<Result>: UseCase<Unit, Single<Result>> {
    operator fun invoke(): Single<Result> {
        return invoke(Unit)
    }
}

interface SingleDataUseCase<Params, Result>: UseCase<Params, Single<Data<Result>>>

interface SingleSimpleDataUseCase<Result>: SingleSimpleUseCase<Data<Result>>



interface MaybeUseCase<Params, Result>: UseCase<Params, Maybe<Result>>

interface MaybeSimpleUseCase<Result>: UseCase<Unit, Maybe<Result>> {
    operator fun invoke(): Maybe<Result> {
        return invoke(Unit)
    }
}

interface MaybeDataUseCase<Params, Result>: UseCase<Params, Maybe<Data<Result>>>

interface MaybeSimpleDataUseCase<Result>: MaybeSimpleUseCase<Data<Result>>



interface ObservableUseCase<Params, Result>: UseCase<Params, Observable<Result>>

interface ObservableSimpleUseCase<Result>: UseCase<Unit, Observable<Result>> {
    operator fun invoke(): Observable<Result> {
        return invoke(Unit)
    }
}

interface ObservableDataUseCase<Params, Result>: UseCase<Params, Observable<Data<Result>>>

interface ObservableSimpleDataUseCase<Result>: ObservableSimpleUseCase<Data<Result>>