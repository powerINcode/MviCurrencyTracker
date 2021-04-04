package com.example.core.domain

import com.example.core.test.*
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DataManagerTest {
    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    val rxRule: CoroutineTestRule = CoroutineTestRule()

    private val delegate: com.example.core.domain.datadelegate.DataDelegate<Unit, Unit> = mock()

    private lateinit var dataManager: com.example.core.domain.datadelegate.DataManager<Unit, Unit>

    @Before
    fun setup() = runBlocking {
        whenever(delegate.putToMemory(any())).thenReturnComplete()
        whenever(delegate.putToStorage(any())).thenReturnComplete()

        dataManager = com.example.core.domain.datadelegate.DataManager(delegate)
    }

    @Test
    fun `observe data | memory and local storage empty | fetch from network, update storage, emit value`() =
        runBlocking {
            whenever(delegate.getFromMemory()).thenReturnEmpty()
            whenever(delegate.getFromStorage()).thenReturnEmpty()
            whenever(delegate.getFromNetwork(any())).thenReturnComplete()

            dataManager.observe(forceReload = true, params = Unit).test(this) {
                assertValuesCount(2)
                assertValueAt(0, com.example.core.domain.datadelegate.Data.Loading())
                assertValueAt(1, com.example.core.domain.datadelegate.Data.Complete(Unit))
            }

            verify(delegate).getFromMemory()
            verify(delegate).getFromStorage()
            verify(delegate).getFromNetwork(Unit)
            verify(delegate).putToMemory(Unit)
            verify(delegate).putToStorage(Unit)
        }

    @Test
    fun `observe data | memory and local storage empty and network fail | emit data error`() =
        runBlocking {
            val error = IllegalStateException()
            whenever(delegate.getFromMemory()).thenReturnEmpty()
            whenever(delegate.getFromStorage()).thenReturnEmpty()
            whenever(delegate.getFromNetwork(any())).thenEmitError(error)

            dataManager.observe(forceReload = true, params = Unit).test(this) {
                assertValuesCount(2)
                assertValueAt(0, com.example.core.domain.datadelegate.Data.Loading())
                assertValueAt(1, com.example.core.domain.datadelegate.Data.Error(error))
            }

            verify(delegate).getFromMemory()
            verify(delegate).getFromStorage()
            verify(delegate).getFromNetwork(Unit)
        }

    @Test
    fun `observe data | local storage empty | update storage, emit value`() = runBlocking {
        whenever(delegate.getFromMemory()).thenReturnEmpty()
        whenever(delegate.getFromStorage()).thenReturn(Unit)
        whenever(delegate.getFromNetwork(any())).thenReturn(Unit)

        dataManager.observe(forceReload = false, params = Unit).test(this) {
            assertValuesCount(1)
            assertValueAt(0, com.example.core.domain.datadelegate.Data.Complete(Unit))
        }

        verify(delegate).getFromMemory()
        verify(delegate).getFromStorage()
        verify(delegate, never()).getFromNetwork(Unit)
        verify(delegate).putToMemory(Unit)
        verify(delegate).putToStorage(Unit)
    }

    @Test
    fun `observe data | memory and local storage have a value | emit value`() = runBlocking {
        whenever(delegate.getFromMemory()).thenReturn(Unit)
        whenever(delegate.getFromStorage()).thenReturn(Unit)
        whenever(delegate.getFromNetwork(any())).thenReturn(Unit)

        dataManager.observe(forceReload = false, params = Unit).test(this) {
            assertValuesCount(1)
            assertValueAt(0, com.example.core.domain.datadelegate.Data.Complete(Unit))
        }

        verify(delegate).getFromMemory()
        verify(delegate, never()).getFromStorage()
        verify(delegate, never()).getFromNetwork(Unit)
        verify(delegate, never()).putToMemory(Unit)
        verify(delegate, never()).putToStorage(Unit)
    }

    @Test
    fun `observe data | memory and local storage have a value and force reload is true | fetch new data`() =
        runBlocking {
            whenever(delegate.getFromMemory()).thenReturn(Unit)
            whenever(delegate.getFromStorage()).thenReturn(Unit)
            whenever(delegate.getFromNetwork(any())).thenReturn(Unit)

            dataManager.observe(forceReload = true, params = Unit).test(this) {
                assertValuesCount(2)
                assertValueAt(0, com.example.core.domain.datadelegate.Data.Loading(Unit))
                assertValueAt(1, com.example.core.domain.datadelegate.Data.Complete(Unit))
            }


            verify(delegate).getFromMemory()
            verify(delegate, never()).getFromStorage()
            verify(delegate).getFromNetwork(Unit)
            verify(delegate).putToMemory(Unit)
            verify(delegate).putToStorage(Unit)
        }

    @Test
    fun `observe data | update values of delegate form outside | emit existed data and then emit updated value`() =
        runBlocking {
            whenever(delegate.getFromMemory()).thenReturn(Unit)
            whenever(delegate.getFromStorage()).thenReturn(Unit)
            whenever(delegate.getFromNetwork(any())).thenReturn(Unit)

            val testObserver = dataManager.observe(forceReload = false, params = Unit).test(this)
                .assertValuesCount(1)
                .assertValueAt(0, com.example.core.domain.datadelegate.Data.Complete(Unit))


            verify(delegate).getFromMemory()
            verify(delegate, never()).getFromStorage()
            verify(delegate, never()).getFromNetwork(Unit)
            verify(delegate, never()).putToMemory(Unit)
            verify(delegate, never()).putToStorage(Unit)

            dataManager.update(Unit)

            verify(delegate).putToMemory(Unit)
            verify(delegate).putToStorage(Unit)

            testObserver.awaitChange()
                .assertValueAt(1, com.example.core.domain.datadelegate.Data.Complete(Unit))
                .finish()
        }
}