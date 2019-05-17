package com.rusmyhal.worldclock.model.system

import io.reactivex.Scheduler

interface SchedulersProvider {

    val io: Scheduler

    val ui: Scheduler
}