package com.example.feature_rate_tracker_impl.delegates

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.activity.recyclerview.RecyclerViewDelegate
import com.example.feature_rate_tracker_impl.R
import com.example.feature_rate_tracker_impl.databinding.ItemAdvertisementBinding

class AdvertisementDelegate : RecyclerViewDelegate(R.layout.item_advertisement) {
    override fun suitFor(target: RecyclerViewDelegate.Model): Boolean = target is Model

    override fun create(view: View): RecyclerView.ViewHolder = ViewHolder(view)

    inner class ViewHolder(view: View): RecyclerViewDelegate.ViewHolder<Model, ItemAdvertisementBinding>(view) {
        override fun bindViewBinding(view: View): ItemAdvertisementBinding = ItemAdvertisementBinding.bind(view)

        override fun bind(item: Model, viewBinding: ItemAdvertisementBinding) {
            viewBinding.advertisementNameTextView.text = item.title
        }

    }

    data class Model(
        override val id: String,
        val title: String
    ) : RecyclerViewDelegate.Model
}