package com.rusmyhal.worldclock.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rusmyhal.worldclock.model.repository.TimeZonesRepository
import com.rusmyhal.worldclock.ui.timezones.TimeZonesViewModel

class ViewModelFactory private constructor(
    private val timeZonesRepository: TimeZonesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        when {
            modelClass.isAssignableFrom(TimeZonesViewModel::class.java) -> TimeZonesViewModel(timeZonesRepository)
            else -> throw IllegalArgumentException("Unknown ViewModel ${modelClass.simpleName}")
        } as T

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance() =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(TimeZonesRepository.getInstance()).also { INSTANCE = it }
            }
    }
}