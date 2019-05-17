package com.rusmyhal.worldclock.model.data.remote.entity

import com.google.gson.annotations.SerializedName

class TimeZonesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("zones") val timeZones: List<TimeZone>
)