package com.example.core.usecase

import com.example.core.data.datadelegate.Data
import kotlinx.coroutines.flow.Flow

interface UseCase<Params, Result> {
    suspend operator fun invoke(params: Params): Result
}

interface SimpleUseCase<Result>: UseCase<Unit, Result> {
    suspend operator fun invoke(): Result {
        return invoke(Unit)
    }
}

interface FlowUseCase<Params, Result>: UseCase<Params, Flow<Result>>

interface FlowSimpleUseCase<Result>: FlowUseCase<Unit, Result> {
    suspend operator fun invoke(): Flow<Result> {
        return invoke(Unit)
    }
}

interface FlowDataUseCase<Params, Result>: UseCase<Params, Flow<Data<Result>>>

interface FlowSimpleDataUseCase<Result>: FlowSimpleUseCase<Data<Result>>