package com.example.feature_rate_tracker_impl.delegates

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.activity.recyclerview.RecyclerViewDelegate
import com.example.feature_rate_tracker_api.data.models.Currency
import com.example.feature_rate_tracker_impl.R
import com.example.feature_rate_tracker_impl.databinding.ItemRateBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RateDelegate : RecyclerViewDelegate(R.layout.item_rate) {

    private val _clickFlow: PublishSubject<Model> = PublishSubject.create()
    val clickFlow: Observable<Model> = _clickFlow

    private val _changeFlow: PublishSubject<Double> = PublishSubject.create()
    val changeFlow: Observable<Double> = _changeFlow

    override fun suitFor(target: RecyclerViewDelegate.Model): Boolean = target is Model

    override fun create(view: View): RecyclerView.ViewHolder = Holder(view)

    data class Model(
        override val id: String,
        val name: String,
        val amount: Double,
        val rate: Double
    ) : RecyclerViewDelegate.Model

    inner class Holder(
        view: View
    ) : RecyclerViewDelegate.ViewHolder<Model, ItemRateBinding>(view) {

        private var watcher: TextWatcher? = null

        override fun bindViewBinding(view: View): ItemRateBinding = ItemRateBinding.bind(view)

        override fun bind(item: Model, viewBinding: ItemRateBinding) {
            val value = String.format("%.2f", item.amount)

            clearEditListener()

            with(viewBinding) {
                currencyTextView.text = item.name

                if (adapterPosition == 0) {
                    if (!ediCurrencyEditText.isFocused) {
                        ediCurrencyEditText.setText(value)
                    }
                    setEditListener()
                    itemView.setOnClickListener { }

                } else {
                    ediCurrencyEditText.setText(value)
                    itemView.setOnClickListener { ediCurrencyEditText.requestFocus() }
                    ediCurrencyEditText.setOnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            _clickFlow.onNext(item)
                        }
                    }
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
                    _changeFlow.onNext(value?.replace(",", ".")?.toDouble() ?: 0.0)
                }

            }
            viewBinding.ediCurrencyEditText.addTextChangedListener(watcher)
        }

        private fun clearEditListener() {
            viewBinding.ediCurrencyEditText.removeTextChangedListener(watcher)
        }
    }
}