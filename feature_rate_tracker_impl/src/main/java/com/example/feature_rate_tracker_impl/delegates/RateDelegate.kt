package com.example.feature_rate_tracker_impl.delegates

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.activity.recyclerview.RecyclerViewDelegate
import com.example.core.coroutine.mutableEventFlow
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_impl.R
import com.example.feature_rate_tracker_impl.databinding.ItemRateBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class RateDelegate : RecyclerViewDelegate(R.layout.item_rate) {

    private val _clickFlow: MutableSharedFlow<Model> = mutableEventFlow()
    val clickFlow: SharedFlow<Model> = _clickFlow

    private val _changeFlow: MutableSharedFlow<Double> = mutableEventFlow()
    val changeFlow: SharedFlow<Double> = _changeFlow

    override fun suitFor(target: RecyclerViewDelegate.Model): Boolean = target is Model

    override fun create(view: View): RecyclerView.ViewHolder = Holder(view)

    data class Model(
        override val id: String,
        val name: String,
        val amount: Double,
        val rate: Double,
        override val extra: Currency
    ) : RecyclerViewDelegate.Model, ExtraContainer<Currency>

    inner class Holder(
        view: View
    ) : RecyclerViewDelegate.ViewHolder<Model, ItemRateBinding>(view) {

        private var watcher: TextWatcher? = null

        private val isSelectedHolder: Boolean get() = adapterPosition == 0

        override val viewBinding: ItemRateBinding by viewBindings(ItemRateBinding::bind)

        override fun onAttachToRecyclerView() {
            super.onAttachToRecyclerView()

            itemView.setOnClickListener {
                viewBinding.ediCurrencyEditText.requestFocus()
            }

            viewBinding.ediCurrencyEditText.setOnFocusChangeListener { v, hasFocus ->
                if (!isSelectedHolder && hasFocus) {
                    _clickFlow.tryEmit(model)
                }
            }

            setEditListener()
        }

        override fun onDetachToRecyclerView() {
            super.onDetachToRecyclerView()

            viewBinding.ediCurrencyEditText.removeTextChangedListener(watcher)
        }

        override fun bind(item: Model) {
            val value = String.format("%.2f", item.amount)

            with(viewBinding) {
                currencyTextView.text = item.name

                if (adapterPosition == 0) {
                    if (!ediCurrencyEditText.isFocused) {
                        ediCurrencyEditText.setText(value)
                    }

                } else {
                    ediCurrencyEditText.setText(value)
                }
            }
        }

        private fun setEditListener() {
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
                   if (isSelectedHolder) {
                       _changeFlow.tryEmit(value?.replace(",", ".")?.toDouble() ?: 0.0)
                   }
                }

            }
            viewBinding.ediCurrencyEditText.addTextChangedListener(watcher)
        }
    }
}