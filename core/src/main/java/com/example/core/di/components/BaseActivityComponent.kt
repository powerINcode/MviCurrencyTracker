package com.example.core.di.components

import androidx.appcompat.app.AppCompatActivity

interface BaseActivityComponent<T: AppCompatActivity> {
    fun inject(activity: T)
}