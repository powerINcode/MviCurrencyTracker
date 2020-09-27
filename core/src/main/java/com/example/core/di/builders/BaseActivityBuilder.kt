package com.example.core.di.builders

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance

interface BaseActivityBuilder<Component, Activity: AppCompatActivity, Builder> {
    @BindsInstance
    fun activity(activity: Activity): Builder

    fun build(): Component
}