package com.example.core.domain

import com.example.core.test.*
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DataManagerTest {
    @Rule
    @JvmField
    val rxRule: RxJavaTestRule = RxJavaTestRule()

    private val delegate: com.example.core.domain.datadelegate.DataDelegate<Unit, Unit> = mock()

    private lateinit var dataManager: com.example.core.domain.datadelegate.DataManager<Unit, Unit>

    @Before
    fun setup() {
        whenever(delegate.putToMemory(any())).thenEmitComplete()
        whenever(delegate.putToStorage(any())).thenEmitComplete()

        dataManager = com.example.core.domain.datadelegate.DataManager(delegate)
    }

    @Test
    fun `observe data | memory and local storage empty | fetch from network, update storage, emit value`() {
        whenever(delegate.getFromMemory()).thenEmitEmpty()
        whenever(delegate.getFromStorage()).thenEmitEmpty()
        whenever(delegate.getFromNetwork(any())).thenEmit(Unit)

        val testObserver = dataManager.observe(forceReload = true, params = Unit).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, com.example.core.domain.datadelegate.Data.Loading())
        testObserver.assertValueAt(1, com.example.core.domain.datadelegate.Data.Complete(Unit))

        verify(delegate).getFromMemory()
        verify(delegate).getFromStorage()
        verify(delegate).getFromNetwork(Unit)
        verify(delegate).putToMemory(Unit)
        verify(delegate).putToStorage(Unit)
    }

    @Test
    fun `observe data | memory and local storage empty and network fail | emit data error`() {
        val error = IllegalStateException()
        whenever(delegate.getFromMemory()).thenEmitEmpty()
        whenever(delegate.getFromStorage()).thenEmitEmpty()
        whenever(delegate.getFromNetwork(any())).thenEmitError(error)

        val testObserver = dataManager.observe(forceReload = true, params = Unit).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, com.example.core.domain.datadelegate.Data.Loading())
        testObserver.assertValueAt(1, com.example.core.domain.datadelegate.Data.Error(error))

        verify(delegate).getFromMemory()
        verify(delegate).getFromStorage()
        verify(delegate).getFromNetwork(Unit)
    }

    @Test
    fun `observe data | local storage empty | update storage, emit value`() {
        whenever(delegate.getFromMemory()).thenEmitEmpty()
        whenever(delegate.getFromStorage()).thenEmit(Unit)
        whenever(delegate.getFromNetwork(any())).thenEmit(Unit)

        val testObserver = dataManager.observe(forceReload = false, params = Unit).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, com.example.core.domain.datadelegate.Data.Complete(Unit))

        verify(delegate).getFromMemory()
        verify(delegate).getFromStorage()
        verify(delegate, never()).getFromNetwork(Unit)
        verify(delegate).putToMemory(Unit)
        verify(delegate).putToStorage(Unit)
    }

    @Test
    fun `observe data | memory and local storage have a value | emit value`() {
        whenever(delegate.getFromMemory()).thenEmit(Unit)
        whenever(delegate.getFromStorage()).thenEmit(Unit)
        whenever(delegate.getFromNetwork(any())).thenEmit(Unit)

        val testObserver = dataManager.observe(forceReload = false, params = Unit).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, com.example.core.domain.datadelegate.Data.Complete(Unit))

        verify(delegate).getFromMemory()
        verify(delegate, never()).getFromStorage()
        verify(delegate, never()).getFromNetwork(Unit)
        verify(delegate, never()).putToMemory(Unit)
        verify(delegate, never()).putToStorage(Unit)
    }

    @Test
    fun `observe data | memory and local storage have a value and force reload is true | fetch new data`() {
        whenever(delegate.getFromMemory()).thenEmit(Unit)
        whenever(delegate.getFromStorage()).thenEmit(Unit)
        whenever(delegate.getFromNetwork(any())).thenEmit(Unit)

        val testObserver = dataManager.observe(forceReload = true, params = Unit).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(2)
        testObserver.assertValueAt(0, Data.Loading(Unit))
        testObserver.assertValueAt(1, Data.Complete(Unit))

        verify(delegate).getFromMemory()
        verify(delegate, never()).getFromStorage()
        verify(delegate).getFromNetwork(Unit)
        verify(delegate).putToMemory(Unit)
        verify(delegate).putToStorage(Unit)
    }

    @Test
    fun `observe data | update values of delegate form outside | emit existed data and then emit updated value`() {
        whenever(delegate.getFromMemory()).thenEmit(Unit)
        whenever(delegate.getFromStorage()).thenEmit(Unit)
        whenever(delegate.getFromNetwork(any())).thenEmit(Unit)

        val testObserver = dataManager.observe(forceReload = false, params = Unit).test()

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, Data.Complete(Unit))

        verify(delegate).getFromMemory()
        verify(delegate, never()).getFromStorage()
        verify(delegate, never()).getFromNetwork(Unit)
        verify(delegate, never()).putToMemory(Unit)
        verify(delegate, never()).putToStorage(Unit)

        dataManager.update(Unit).test()
            .assertComplete()

        verify(delegate).putToMemory(Unit)
        verify(delegate).putToStorage(Unit)

        testObserver.assertValueAt(1, com.example.core.domain.datadelegate.Data.Complete(Unit))

    }
}