package com.rusmyhal.worldclock.model.data.remote

import com.rusmyhal.worldclock.model.data.remote.entity.TimeZonesResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("$VERSION/list-time-zone")
    fun getTimeZones(): Single<TimeZonesResponse>

    companion object {

        const val VERSION = "v2.1"
    }
}