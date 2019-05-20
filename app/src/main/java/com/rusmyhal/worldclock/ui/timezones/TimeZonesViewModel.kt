package com.rusmyhal.worldclock.ui.timezones

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rusmyhal.worldclock.R
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

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarMessage

    private val _selectedTimeZone = MutableLiveData<TimeZone>()
    val selectedTimeZone: LiveData<TimeZone>
        get() = _selectedTimeZone

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    override fun onCleared() {
        compositeDisposable.clear()
    }

    fun fetchTimeZones() {
        timeZonesRepository.fetchTimeZones()
            .doOnSubscribe { _progress.value = true }
            .doFinally { _progress.value = false }
            .subscribe({ response ->
                response.sortedBy { it.zoneName }.also { sortedZones ->
                    _timeZonesList.value = sortedZones.map { it.zoneName }
                    timeZonesResponse.value = sortedZones
                }
            }, { error ->
                _snackbarMessage.value = Event(R.string.error_api)
                error.printStackTrace()
            })
            .addTo(compositeDisposable)
    }

    fun onTimeZoneSelected(position: Int) {
        _selectedTimeZone.value = timeZonesResponse.value?.get(position)
    }
}