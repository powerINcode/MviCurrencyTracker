package com.example.feature_rate_tracker_impl

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.feature_rate_tracker_impl.MainScreenContract.*
import kotlinx.android.synthetic.main.item_rate.view.*

internal class RateAdapter : RecyclerView.Adapter<RateAdapter.Holder>() {
    private val items: MutableList<ScreenCurrency> = mutableListOf()

    var onClick: ((ScreenCurrency) -> Unit)? = null
    var onChange: ((Double) -> Unit)? = null

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.item_rate, parent, false)
        )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.clearEditListener()

        holder.textView.text = item.name


        val value = String.format("%.2f", item.amount)
        if (position == 0) {
            if (!holder.editText.isFocused) {
                holder.editText.setText(value)
            }
            holder.setEditListener(onChange)
            holder.itemView.setOnClickListener { }

        } else {
            holder.editText.setText(value)
            holder.itemView.setOnClickListener { holder.editText.requestFocus() }
            holder.editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    onClick?.invoke(item)
                }
            }
        }
    }

    fun swap(data: List<ScreenCurrency>) {
        val diffUtilCallback = object: DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = items[oldItemPosition].name == data[newItemPosition].name

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = items[oldItemPosition] == data[newItemPosition]

        }

        val result = DiffUtil.calculateDiff(diffUtilCallback)
        items.clear()
        items.addAll(data)

        result.dispatchUpdatesTo(this)

    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = itemView.currencyTextView
        val editText = itemView.ediCurrencyEditText

        private var watcher: TextWatcher? = null

        fun setEditListener(block: ((Double) -> Unit)?) {
            editText.removeTextChangedListener(watcher)
            watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val value = if (s.isNullOrEmpty()) null else s.toString()
                    block?.invoke(value?.replace(",", ".")?.toDouble() ?: 0.0)
                }

            }
            editText.addTextChangedListener(watcher)
        }

        fun clearEditListener() {
            editText.removeTextChangedListener(watcher)
        }
    }
}