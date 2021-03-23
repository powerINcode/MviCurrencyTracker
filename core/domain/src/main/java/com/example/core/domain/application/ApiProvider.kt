package com.example.core.application


interface ApiProvider {
    fun <T: Any> getApi(api: Class<T>): T
}