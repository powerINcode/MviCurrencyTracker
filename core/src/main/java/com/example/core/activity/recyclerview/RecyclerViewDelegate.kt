package com.example.core.activity.recyclerview

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class RecyclerViewDelegate(
    @LayoutRes val layoutId: Int
) {
    abstract fun suitFor(target: Model): Boolean
    fun suitFor(layoutId: Int): Boolean = this.layoutId == layoutId

    abstract fun create(view: View): RecyclerView.ViewHolder

    abstract class ViewHolder<T : Model, V : ViewBinding>(view: View) : RecyclerView.ViewHolder(view) {

        protected abstract fun bindViewBinding(view: View): V

        protected lateinit var viewBinding: V
        protected lateinit var model: T

        open fun bind(item: Model) {
            model = item as T
            viewBinding = bindViewBinding(itemView)
            bind(model, viewBinding)
        }

        protected abstract fun bind(item: T, viewBinding: V)
        fun onAttachToRecyclerView() {}
        fun onDetachToRecyclerView() {}
    }

    interface Model {
        val id: String
    }
}