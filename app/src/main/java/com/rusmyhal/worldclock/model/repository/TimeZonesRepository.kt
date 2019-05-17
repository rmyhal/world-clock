package com.rusmyhal.worldclock.model.repository

import com.rusmyhal.worldclock.model.data.remote.ApiService
import com.rusmyhal.worldclock.model.data.remote.entity.TimeZone
import com.rusmyhal.worldclock.model.system.SchedulersProvider
import io.reactivex.Single

class TimeZonesRepository(private val api: ApiService, private val schedulers: SchedulersProvider) {

    fun fetchTimeZones(): Single<List<TimeZone>> {
        return api.getTimeZones()
            .observeOn(schedulers.ui)
            .subscribeOn(schedulers.io)
            .map { response -> response.timeZones }
    }
}