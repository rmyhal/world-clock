package com.rusmyhal.worldclock.ui.timezones

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rusmyhal.worldclock.model.repository.TimeZonesRepository
import com.rusmyhal.worldclock.util.addTo
import io.reactivex.disposables.CompositeDisposable

class TimeZonesViewModel(private val timeZonesRepository: TimeZonesRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun fetchTimeZones() {
        timeZonesRepository.fetchTimeZones()
            .subscribe({ response ->
                Log.d(TAG, "$response")
            }, { error ->
                error.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    companion object {
        const val TAG = "TimeZonesViewModel"
    }
}