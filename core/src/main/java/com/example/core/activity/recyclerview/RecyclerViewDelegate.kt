package com.example.core.activity.recyclerview

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class RecyclerViewDelegate(
    @LayoutRes val layoutId: Int
) {
    abstract fun suitFor(target: Model): Boolean

    fun suitFor(layoutId: Int): Boolean = this.layoutId == layoutId

    abstract fun create(view: View): RecyclerView.ViewHolder

    abstract class ViewHolder<T : Model, V : ViewBinding>(view: View) : RecyclerView.ViewHolder(view) {

        protected abstract val viewBinding: V
        protected lateinit var model: T

        fun bindItem(item: Model) {
            model = item as T
            bind(model)
        }

        protected abstract fun bind(item: T)

        open fun onAttachToRecyclerView() {}

        open fun onDetachToRecyclerView() {}

        protected fun <T> viewBindings(binder: (View) -> T) = ViewHolderBindingReadProperty(binder)

        protected class ViewHolderBindingReadProperty<T>(val binder: (View) -> T): ReadOnlyProperty<RecyclerView.ViewHolder, T> {
            override fun getValue(thisRef: RecyclerView.ViewHolder, property: KProperty<*>): T {
                return binder(thisRef.itemView)
            }

        }
    }

    interface Model {
        val id: String
    }

    interface ExtraContainer<T> {
        val extra: T
    }
}