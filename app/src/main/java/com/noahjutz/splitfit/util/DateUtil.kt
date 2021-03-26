package com.noahjutz.splitfit.util

import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import kotlin.time.seconds

@OptIn(ExperimentalTime::class)
infix operator fun Date.minus(date: Date): Duration = (this.time - date.time).milliseconds

/**
 * Sum of all [Duration] items in a list
 */
@ExperimentalTime
val List<Duration>.total: Duration
    get() {
        var total = 0.seconds
        forEach {
            total += it
        }
        return total
    }

@ExperimentalTime
val List<Duration>.average: Duration
    get() = total / size

/**
 * Number of consecutive daily workouts including today
 */
val List<Date>.longestDailyStreak: Int
    get() {
        val days = map { it.day }
        val today = Calendar.getInstance().time.day
        if (days.isEmpty()) return 0
        if (days[0] != today) return 0
        var streak = 1
        days.forEachIndexed { i, d ->
            if (i == 0) return@forEachIndexed
            if (days[i-1] == d - 1) streak++
        }
        return streak
    }
