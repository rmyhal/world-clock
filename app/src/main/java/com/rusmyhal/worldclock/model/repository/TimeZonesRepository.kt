package com.rusmyhal.worldclock.model.repository

import com.rusmyhal.worldclock.BuildConfig
import com.rusmyhal.worldclock.model.data.remote.ApiService
import com.rusmyhal.worldclock.model.data.remote.RetrofitFactory
import com.rusmyhal.worldclock.model.data.remote.entity.TimeZone
import com.rusmyhal.worldclock.model.system.AppSchedulers
import com.rusmyhal.worldclock.model.system.SchedulersProvider
import io.reactivex.Single

class TimeZonesRepository private constructor(private val api: ApiService, private val schedulers: SchedulersProvider) {

    fun fetchTimeZones(): Single<List<TimeZone>> {
        return api.getTimeZones()
            .observeOn(schedulers.ui)
            .subscribeOn(schedulers.io)
            .map { response -> response.timeZones }
    }

    companion object {

        @Volatile
        private var INSTANCE: TimeZonesRepository? = null

        fun getInstance() =
            INSTANCE ?: synchronized(TimeZonesRepository::class.java) {
                INSTANCE ?: TimeZonesRepository(
                    RetrofitFactory.createService(ApiService::class.java, BuildConfig.TIME_ZONE_ENDPOINT),
                    AppSchedulers()
                ).also { INSTANCE = it }
            }
    }
}