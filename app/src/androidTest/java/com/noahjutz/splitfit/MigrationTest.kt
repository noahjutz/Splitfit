package com.noahjutz.splitfit

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.noahjutz.splitfit.data.AppDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class MigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )


    @Test
    @Throws(IOException::class)
    fun migrate35To36() {
        var db = helper.createDatabase(TEST_DB, 35).use {
            it.execSQL("INSERT INTO exercise_table VALUES ('Squat', 'true', 'true', 'false', 'false', 'false', 0)")
            it
        }

        db = helper.runMigrationsAndValidate(TEST_DB, 36, true)
    }
}