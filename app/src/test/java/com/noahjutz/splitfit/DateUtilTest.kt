package com.noahjutz.splitfit

import com.noahjutz.splitfit.util.currentDailyStreak
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.hours

@ExperimentalTime
class DateUtilTest {

    private val now = Calendar.getInstance().time

    private val dates5Streak = listOf(
        now,
        Date((now.time - 24.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 72.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
    )

    private val dates3StreakInterrupted = listOf(
        now,
        Date((now.time - 24.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
    )

    private val datesNoStreak = listOf(
        Date((now.time - 24.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 72.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
    )

    private val dates5StreakMultipleADay = listOf(
        now,
        Date((now.time - 24.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 24.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 72.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
    )

    private val datesNoStreak2 = listOf(
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 72.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 110.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 134.hours.absoluteValue.inMilliseconds).toLong()),
    )

    @Test
    fun `5 Day streak`() {
        val streak = dates5Streak.currentDailyStreak
        assertEquals(5, streak)
    }

    @Test
    fun `3 Day streak`() {
        val streak = dates5Streak.subList(0, 3).currentDailyStreak
        assertEquals(3, streak)
    }

    @Test
    fun `3 Day streak with fourth day seperated by gap`() {
        val streak = dates3StreakInterrupted.currentDailyStreak
        assertEquals(3, streak)
    }

    @Test
    fun `1 Day streak`() {
        val streak = dates5Streak.subList(0, 1).currentDailyStreak
        assertEquals(1, streak)
    }

    @Test
    fun `No workout today, no streak`() {
        val streak = datesNoStreak.currentDailyStreak
        assertEquals(0, streak)
    }

    @Test
    fun `No streak 2`() {
        val streak = datesNoStreak2.currentDailyStreak
        assertEquals(0, streak)
    }

    @Test
    fun `1 Day streak 2`() {
        val streak = listOf(
            Date(1616713200000),
            Date(0),
            Date(1616841912690)
        ).currentDailyStreak
        assertEquals(1, streak)
    }

    @Test
    fun `Empty workout list, no streak`() {
        val streak = emptyList<Date>().currentDailyStreak
        assertEquals(0, streak)
    }

    @Test
    fun `5 Day streak with multiple dates per day`() {
        val streak = dates5StreakMultipleADay.currentDailyStreak
        assertEquals(5, streak)
    }
}