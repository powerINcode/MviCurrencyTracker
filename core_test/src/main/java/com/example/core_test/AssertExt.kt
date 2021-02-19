package com.example.core_test

import org.junit.Assert

fun <T> T.assertEquals(expected: T) = Assert.assertEquals(expected, this)
fun <T> List<T>.assertValueAt(index: Int, expected: T) = Assert.assertEquals(expected, this[index])