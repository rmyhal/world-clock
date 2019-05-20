package com.rusmyhal.worldclock.model.data.remote

import com.rusmyhal.worldclock.BuildConfig
import com.rusmyhal.worldclock.model.data.remote.entity.TimeZonesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("$VERSION/list-time-zone")
    fun getTimeZones(
        @Query("format") format: String = "json",
        @Query("key") apiKey: String = BuildConfig.TIME_ZONE_API_KEY
    ): Single<TimeZonesResponse>

    companion object {

        const val VERSION = "v2.1"
    }
}