package com.rusmyhal.worldclock.model.data.remote.entity

import com.google.gson.annotations.SerializedName

data class TimeZone(
    @SerializedName("countryName") val countryName: String,
    @SerializedName("zoneName") val zoneName: String,
    @SerializedName("timestamp") val timestamp: Long
)