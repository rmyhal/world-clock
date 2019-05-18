package com.rusmyhal.worldclock.ui.timezones

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rusmyhal.worldclock.R
import com.rusmyhal.worldclock.util.viewModel

class TimeZonesActivity : AppCompatActivity() {

    private val viewModel: TimeZonesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.fetchTimeZones()
    }
}
