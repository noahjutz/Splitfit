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
    get() = if (size > 0) total / size else 0.seconds

/**
 * Number of consecutive daily workouts including today
 */
val List<Date>.currentDailyStreak: Int
    get() {
        val today = (Calendar.getInstance().time.roundToDay()).toInt()
        var streak = 0
        map { it.roundToDay() }.sortedDescending().forEach { day ->
            if (day == today - streak) streak++
            else if (day < today - streak) return streak
        }
        return streak
    }

/**
 * Returns number of days starting from 1970-01-01
 */
fun Date.roundToDay(): Int = (time * 0.00000001157407).toInt()
