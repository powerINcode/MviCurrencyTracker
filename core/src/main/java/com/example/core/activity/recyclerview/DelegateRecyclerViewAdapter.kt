package com.example.core.activity.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class DelegateRecyclerViewAdapter(
    private val delegates: List<RecyclerViewDelegate>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: MutableList<RecyclerViewDelegate.Model> = mutableListOf()

    override fun getItemViewType(position: Int): Int {
        return delegates.firstOrNull { delegate -> delegate.suitFor(items[position]) }?.layoutId
            ?: throw IllegalStateException("Can't find RecyclerViewDelegate for the model: ${items[position]}")
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate = delegates.firstOrNull { delegate -> delegate.suitFor(viewType) }
            ?: throw IllegalStateException("Can't find RecyclerViewDelegate for the: $viewType")

        val view = LayoutInflater.from(parent.context).inflate(delegate.layoutId, parent, false)
        return delegate.create(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as? RecyclerViewDelegate.ViewHolder<*, *>)?.bindItem(item)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as? RecyclerViewDelegate.ViewHolder<*, *>)?.onAttachToRecyclerView()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as? RecyclerViewDelegate.ViewHolder<*, *>)?.onDetachToRecyclerView()
    }

    fun swap(data: List<RecyclerViewDelegate.Model>) {
        val diffUtilCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = data[newItemPosition]
                return oldItem.id == newItem.id
            }

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition] == data[newItemPosition]

        }

        val result = DiffUtil.calculateDiff(diffUtilCallback)
        items.clear()
        items.addAll(data)

        result.dispatchUpdatesTo(this)

    }
}