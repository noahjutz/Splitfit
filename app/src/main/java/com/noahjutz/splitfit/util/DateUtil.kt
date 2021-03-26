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
        return -1 // TODO
    }
