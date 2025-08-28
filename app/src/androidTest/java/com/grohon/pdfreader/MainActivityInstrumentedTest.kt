package com.grohon.pdfreader

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.grohon.pdfreader", appContext.packageName)
    }

    @Test
    fun mainActivity_launches_successfully() {
        // Given - activity is launched via ActivityScenarioRule
        // When - we check if activity is created
        activityScenarioRule.scenario.onActivity { activity ->
            // Then - activity should not be null and should be MainActivity
            assertNotNull(activity)
            assertTrue(activity is MainActivity)
        }
    }
}