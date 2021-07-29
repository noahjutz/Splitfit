package com.noahjutz.splitfit.util

import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

private val prettyTime = PrettyTime()

fun Date.pretty(): String = prettyTime.format(this)

@OptIn(ExperimentalTime::class)
fun Duration.pretty(): String = this.toComponents { h, m, _, _ -> "${h}h ${m}min" }

@OptIn(ExperimentalTime::class)
infix operator fun Date.minus(date: Date): Duration = Duration.milliseconds(this.time - date.time)

@OptIn(ExperimentalTime::class)
fun Duration.iso8601() = toComponents { hours, minutes, seconds, _ ->
    val formatPart = { it: Int -> if (it > 9) it.toString() else "0$it" }
    val hoursString = formatPart(hours)
    val minutesString = formatPart(minutes)
    val secondsString = formatPart(seconds)
    "$hoursString:$minutesString:$secondsString"
}

/**
 * Sum of all [Duration] items in a list
 */
@ExperimentalTime
val List<Duration>.total: Duration
    get() {
        var total = Duration.seconds(0)
        forEach {
            total += it
        }
        return total
    }

@ExperimentalTime
val List<Duration>.average: Duration
    get() = if (size > 0) total / size else Duration.seconds(0)

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
