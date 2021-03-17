package com.example.feature_rate_tracker_impl

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.activity.recyclerview.BaseRecyclerViewAdapter
import com.example.core.activity.recyclerview.ViewBindingHolder
import com.example.feature_rate_tracker_impl.MainScreenContract.ScreenCurrency
import com.example.feature_rate_tracker_impl.databinding.ItemRateBinding

internal class RateAdapter : BaseRecyclerViewAdapter<ScreenCurrency, ItemRateBinding, RateAdapter.Holder>() {
    var onClick: ((ScreenCurrency) -> Unit)? = null
    var onChange: ((Double) -> Unit)? = null

    override val ScreenCurrency.itemId: Any
        get() {
            return this.name
        }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder =
        Holder(ItemRateBinding.inflate(inflater, parent, false), onClick, onChange)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        holder as Holder
        holder.bind(position == 0, item)
    }

    class Holder(
        viewBinding: ItemRateBinding,
        private val onClick: ((ScreenCurrency) -> Unit)?,
        private val onChange: ((Double) -> Unit)?
    ) : ViewBindingHolder<ItemRateBinding, ScreenCurrency>(viewBinding) {

        private var watcher: TextWatcher? = null

        fun bind(selected: Boolean, item: ScreenCurrency) {
            super.bind(item)

            val value = String.format("%.2f", item.amount)

            clearEditListener()

            with(viewBinding) {
                currencyTextView.text = item.name

                if (selected) {
                    if (!ediCurrencyEditText.isFocused) {
                        ediCurrencyEditText.setText(value)
                    }
                    setEditListener(onChange)
                    itemView.setOnClickListener { }

                } else {
                    ediCurrencyEditText.setText(value)
                    itemView.setOnClickListener { ediCurrencyEditText.requestFocus() }
                    ediCurrencyEditText.setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            onClick?.invoke(item)
                        }
                    }
                }
            }
        }

        private fun setEditListener(block: ((Double) -> Unit)?) {
            viewBinding.ediCurrencyEditText.removeTextChangedListener(watcher)
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
            viewBinding.ediCurrencyEditText.addTextChangedListener(watcher)
        }

        private fun clearEditListener() {
            viewBinding.ediCurrencyEditText.removeTextChangedListener(watcher)
        }
    }
}