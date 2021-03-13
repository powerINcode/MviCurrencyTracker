package com.example.feature_rate_tracker_impl

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
import com.example.core.activity.BaseActivity
import com.example.feature_rate_tracker_impl.MainScreenContract.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity<RateTrackerIntent, RateTrackerState, MainViewModel>() {
    override val viewModel by viewModels<MainViewModel>()

    private val rateAdapter = RateAdapter()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this).inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.info){
            viewModel.send(RateTrackerIntent.NavigateToInfo)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(currencyRecyclerView) {
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

        rateAdapter.onClick = { viewModel.send(RateTrackerIntent.CurrencySelected(it)) }
        rateAdapter.onChange = { viewModel.send(RateTrackerIntent.AmountUpdated(it)) }
    }

    override fun render(state: RateTrackerState) {
        rateAdapter.swap(state.currencies)

        progressView.isVisible = state.loading

        errorHolder.isVisible = state.error
    }
}