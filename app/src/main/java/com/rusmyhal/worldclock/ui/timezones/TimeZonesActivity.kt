package com.rusmyhal.worldclock.ui.timezones

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.rusmyhal.worldclock.R
import com.rusmyhal.worldclock.util.viewModel
import kotlinx.android.synthetic.main.activity_time_zones.*

class TimeZonesActivity : AppCompatActivity() {

    private val viewModel: TimeZonesViewModel by viewModel()

    private val timeZonesAdapter by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_zones)
        timeZonesSpinner.adapter = timeZonesAdapter

        setupListeners()
        initObservers()

        if (savedInstanceState == null) {
            viewModel.fetchTimeZones()
        }
    }

    private fun initObservers() = with(viewModel) {
        timeZonesList.observe(this@TimeZonesActivity, Observer { timeZones ->
            timeZonesAdapter.addAll(timeZones)
        })

        selectedTimeZone.observe(this@TimeZonesActivity, Observer { timeZone ->
            Log.d(TAG, "initObservers() $timeZone")
        })

        snackbarMessage.observe(this@TimeZonesActivity, Observer { message ->
            message.getContentIfNotHandled()?.let {
                Snackbar.make(content, it, Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupListeners() {
        timeZonesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                if (view != null) {
                    viewModel.onTimeZoneSelected(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { /* ignore */
            }
        }
    }

    companion object {

        const val TAG = "TimeZonesActivity"
    }
}
