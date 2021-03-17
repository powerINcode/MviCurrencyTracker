package com.example.core.activity.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<Item, ViewHolderBindings: ViewBinding, ViewHolder: ViewBindingHolder<ViewHolderBindings, Item>>: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val items: MutableList<Item> = mutableListOf()

    protected open val Item.itemId: Any get() = hashCode()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return onCreateViewHolder(LayoutInflater.from(parent.context), parent)
    }

    protected abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (item as? ViewHolder)?.let { it.bind(item) }
    }

    fun swap(data: List<Item>) {
        val diffUtilCallback = object: DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = data[newItemPosition]
                return oldItem.itemId == newItem.itemId
            }

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = items[oldItemPosition] == data[newItemPosition]

        }

        val result = DiffUtil.calculateDiff(diffUtilCallback)
        items.clear()
        items.addAll(data)

        result.dispatchUpdatesTo(this)

    }
}