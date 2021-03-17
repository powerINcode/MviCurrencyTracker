package com.example.core.activity.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ViewBindingHolder<T: ViewBinding, T2>(protected val viewBinding: T): RecyclerView.ViewHolder(viewBinding.root) {
    protected var item: T2? = null

    open fun bind(item: T2) {
        this.item = item
    }
}