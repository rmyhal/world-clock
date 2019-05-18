package com.rusmyhal.worldclock.util

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.rusmyhal.worldclock.ui.ViewModelFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewModelDelegate<T : ViewModel>(private val modelClass: Class<T>) : ReadOnlyProperty<FragmentActivity, T> {
    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        return ViewModelProviders.of(thisRef, ViewModelFactory.getInstance()).get(modelClass)
    }
}