package com.example.core_test

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

fun OngoingStubbing<Completable>.thenEmitComplete(): OngoingStubbing<Completable> = this.thenReturn(Completable.complete())
