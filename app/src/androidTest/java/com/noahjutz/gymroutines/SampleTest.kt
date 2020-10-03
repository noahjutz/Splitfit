package com.noahjutz.gymroutines

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.createAndroidComposeRule
import androidx.ui.test.onNodeWithText
import androidx.ui.test.performClick
import com.noahjutz.gymroutines.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class SampleTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun successfulTest() {
        composeTestRule.setContent {
            val num = remember { mutableStateOf(0) }
            Column {
                Button(
                    content = { Text("Click me!") },
                    onClick = { num.value++ }
                )
                Text("number is ${num.value}")
            }
        }

        for (i in 1..20) {
            onNodeWithText("Click me!").performClick()
            onNodeWithText("number is $i").assertIsDisplayed()
        }
    }
}
