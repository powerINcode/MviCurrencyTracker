package com.example.core.test

import io.reactivex.rxjava3.core.Completable
import org.mockito.stubbing.OngoingStubbing

fun OngoingStubbing<Completable>.thenEmitComplete(): OngoingStubbing<Completable> = this.thenReturn(Completable.complete())
