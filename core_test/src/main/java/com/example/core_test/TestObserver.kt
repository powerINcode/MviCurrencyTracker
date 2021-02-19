package com.example.core_test

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.coroutineContext

class TestObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T>
    ) {
        private val values = mutableListOf<T>()
        private val job: Job = scope.launch {
            flow
                .collect { values.add(it) }
        }
        fun assertNoValues(): TestObserver<T> {
            emptyList<T>().assertEquals(this.values)
            return this
        }
        fun assertValues(vararg values: T): TestObserver<T> {
            values.toList().assertEquals(this.values)
            return this
        }

        suspend fun assertValuesCount(count: Int): TestObserver<T> {
            this.awaitChange(count)
            values.toList().size.assertEquals(count)
            return this
        }

        fun assertValueAt(index: Int, value: T): TestObserver<T> {
            values.toList()[index].assertEquals(value)
            return this
        }

        suspend fun awaitChange(expectedCount: Int? = null): TestObserver<T> {
            val currentCount = values.toList().size
            val target = expectedCount ?: currentCount + 1
            while (target != values.toList().size) {
                coroutineContext.ensureActive()
                delay(10)
            }

            return this
        }

        fun finish() {
            job.cancel()
        }
    }