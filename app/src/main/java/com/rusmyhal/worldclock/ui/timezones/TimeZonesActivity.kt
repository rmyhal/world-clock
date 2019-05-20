package com.rusmyhal.worldclock.ui.timezones

import android.graphics.Color
import android.os.Bundle
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

    private val clockColorAdapter by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
    }

    private val colors by lazy {
        resources.getStringArray(R.array.clockColors)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_zones)

        timeZonesSpinner.adapter = timeZonesAdapter

        clockColorAdapter.addAll(colors.toList())
        clockColorSpinner.adapter = clockColorAdapter

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
            analogClock.setTime(timeZone.zoneName)
        })

        snackbarMessage.observe(this@TimeZonesActivity, Observer { message ->
            message.getContentIfNotHandled()?.let {
                Snackbar.make(content, getString(it), Snackbar.LENGTH_LONG)
                    .setAction(R.string.time_zones_retry) {
                        viewModel.fetchTimeZones()
                    }
                    .show()
            }
        })
        progress.observe(this@TimeZonesActivity, Observer { progress ->
            if (progress) progressBar.show() else progressBar.hide()
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

        clockColorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                updateClockColor(colors[position].toLowerCase())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { /* ignore */
            }

        }
    }

    private fun updateClockColor(color: String) {
        analogClock.clockColor = Color.parseColor(color)
    }
}
