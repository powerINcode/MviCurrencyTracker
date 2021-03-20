package com.example.feature_rate_tracker_impl

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.core.activity.BaseActivity
import com.example.core.activity.recyclerview.DelegateRecyclerViewAdapter
import com.example.core.activity.viewbinding.viewBindings
import com.example.feature_rate_tracker_impl.MainScreenContract.*
import com.example.feature_rate_tracker_impl.databinding.ActivityMainBinding
import com.example.feature_rate_tracker_impl.delegates.AdvertisementDelegate
import com.example.feature_rate_tracker_impl.delegates.RateDelegate
import com.example.feature_rate_tracker_impl.di.RateTrackerActivityComponent
import com.example.feature_rate_tracker_impl.di.RateTrackerFeatureComponent

class MainActivity :
    BaseActivity<RateTrackerActivityComponent, RateTrackerState, MainViewModel, ActivityMainBinding>() {

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override val viewBinding: ActivityMainBinding by viewBindings(ActivityMainBinding::inflate)

    override fun createComponent(): RateTrackerActivityComponent =
        provideApi(RateTrackerFeatureComponent::class.java)
            .getRateTrackerActivityBuilder()
            .activity(this)
            .build()

    override fun inject(component: RateTrackerActivityComponent) {
        component.inject(this)
    }

    private val rateDelegate = RateDelegate()

    private val rateAdapter = DelegateRecyclerViewAdapter(
        delegates = listOf(
            rateDelegate,
            AdvertisementDelegate()
        )
    )

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this).inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.info) {
            viewModel.send(RateTrackerIntent.NavigateToInfo)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(viewBinding.currencyRecyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = rateAdapter
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val ims: InputMethodManager =
                            this@MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        ims.hideSoftInputFromWindow(
                            this@MainActivity.window.decorView.windowToken,
                            0
                        )
                    }
                }
            })
        }

        rateDelegate.clickFlow.subscribeTillDestroy {
            viewModel.send(
                RateTrackerIntent.CurrencySelected(
                    it.extra,
                    it.amount
                )
            )
        }
        rateDelegate.changeFlow.subscribeTillDestroy { viewModel.send(RateTrackerIntent.AmountUpdated(it)) }
    }

    override fun render(state: RateTrackerState) {
        rateAdapter.swap(state.screenItems)

        with(viewBinding) {
            progressView.isVisible = state.loading
            errorHolder.isVisible = state.error
        }
    }
}