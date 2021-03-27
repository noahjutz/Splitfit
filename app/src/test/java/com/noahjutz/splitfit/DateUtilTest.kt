package com.noahjutz.splitfit

import com.noahjutz.splitfit.util.longestDailyStreak
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

    private val datesNoStreak = listOf(
        Date((now.time - 24.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 72.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
    )

    @Test
    fun `5 Day streak`() {
        val streak = dates5Streak.longestDailyStreak
        assertEquals(5, streak)
    }

    @Test
    fun `3 Day streak`() {
        val streak = dates5Streak.subList(0, 3).longestDailyStreak
        assertEquals(3, streak)
    }

    @Test
    fun `1 Day streak`() {
        val streak = dates5Streak.subList(0, 1).longestDailyStreak
        assertEquals(1, streak)
    }

    @Test
    fun `No workout today, no streak`() {
        val streak = datesNoStreak.longestDailyStreak
        assertEquals(0, streak)
    }

    @Test
    fun `Empty workout list, no streak`() {
        val streak = emptyList<Date>().longestDailyStreak
        assertEquals(0, streak)
    }
}