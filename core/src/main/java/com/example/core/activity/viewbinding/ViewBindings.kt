package com.example.core.activity.viewbinding

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> viewBindings(noinline initializer: (LayoutInflater) -> T): ReadOnlyProperty<AppCompatActivity, T> =
    ViewBindingProperty(initializer)

class ViewBindingProperty<T : ViewBinding>(private val initializer: (LayoutInflater) -> T) :
    ReadOnlyProperty<AppCompatActivity, T> {
    private var value: T? = null

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        return value ?: initializer(thisRef.layoutInflater).also { viewBinding -> value = viewBinding }
    }
}