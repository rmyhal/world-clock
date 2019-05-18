package com.rusmyhal.worldclock.ui.timezones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rusmyhal.worldclock.model.data.remote.entity.TimeZone
import com.rusmyhal.worldclock.model.repository.TimeZonesRepository
import com.rusmyhal.worldclock.util.Event
import com.rusmyhal.worldclock.util.addTo
import io.reactivex.disposables.CompositeDisposable

class TimeZonesViewModel(private val timeZonesRepository: TimeZonesRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val timeZonesResponse = MutableLiveData<List<TimeZone>>()

    private val _timeZonesList = MutableLiveData<List<String>>().apply { value = emptyList() }
    val timeZonesList: LiveData<List<String>>
        get() = _timeZonesList

    private val _snackbarMessage = MutableLiveData<Event<String>>()
    val snackbarMessage: LiveData<Event<String>>
        get() = _snackbarMessage

    private val _selectedTimeZone = MutableLiveData<TimeZone>()
    val selectedTimeZone: LiveData<TimeZone>
        get() = _selectedTimeZone

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun fetchTimeZones() {
        timeZonesRepository.fetchTimeZones()
            .subscribe({ response ->
                _timeZonesList.value = response.map { it.zoneName }.also {
                    timeZonesResponse.value = response
                }
            }, { error ->
                _snackbarMessage.value = Event("Something went wrong, try again")
                error.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    fun onTimeZoneSelected(position: Int) {
        _selectedTimeZone.value = timeZonesResponse.value?.get(position)
    }

    companion object {
        const val TAG = "TimeZonesViewModel"
    }
}