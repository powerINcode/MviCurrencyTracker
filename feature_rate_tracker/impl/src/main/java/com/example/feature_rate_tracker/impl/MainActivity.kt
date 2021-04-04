package com.example.feature_rate_tracker.impl

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.core.ui.activity.BaseActivity
import com.example.core.ui.recyclerview.DelegateRecyclerViewAdapter
import com.example.core.ui.viewbinding.viewBindings
import com.example.feature_rate_tracker.R
import com.example.feature_rate_tracker.databinding.ActivityMainBinding
import com.example.feature_rate_tracker.impl.MainScreenContract.*
import com.example.feature_rate_tracker.impl.MainScreenContract.RateTrackerIntent.*
import com.example.feature_rate_tracker.impl.delegates.AdvertisementDelegate
import com.example.feature_rate_tracker.impl.delegates.RateDelegate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<RateTrackerState, MainPresenter, MainViewModel>() {

    override val viewBinding: ActivityMainBinding by viewBindings(ActivityMainBinding::inflate)

    @Inject
    override lateinit var presenter: MainPresenter

    override val viewModel: MainViewModel by viewModels()

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
            presenter.send(NavigateToInfo)
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

        rateDelegate.clickFlow.collectWhenCreated {
            presenter.send(CurrencySelected(it.extra, it.amount))
        }
        rateDelegate.changeFlow.collectWhenCreated {
            presenter.send(AmountUpdated(it))
        }
    }

    override fun render(state: RateTrackerState) {
        rateAdapter.swap(state.screenItems)

        with(viewBinding) {
            progressView.isVisible = state.loading
            errorHolder.isVisible = state.error
        }
    }
}