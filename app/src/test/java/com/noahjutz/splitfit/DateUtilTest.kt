package com.noahjutz.splitfit

import android.util.Log
import com.noahjutz.splitfit.util.longestDailyStreak
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.hours

@ExperimentalTime
class DateUtilTest {

    private val now: Date = Calendar.getInstance().time

    private val dates: List<Date> = listOf(
        now,
        Date((now.time - 24.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 48.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 72.hours.absoluteValue.inMilliseconds).toLong()),
        Date((now.time - 96.hours.absoluteValue.inMilliseconds).toLong()),
    )

    @Test
    fun `5 Day streak`() {
        val streak = dates.longestDailyStreak
        assertEquals(5, streak)
    }
}