package com.rusmyhal.worldclock.util

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import kotlin.properties.ReadOnlyProperty

inline fun <reified T : ViewModel, R : FragmentActivity> FragmentActivity.viewModel(): ReadOnlyProperty<R, T> {
    return ViewModelDelegate(T::class.java)
}