package com.pajato.argusremastered

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Assert
import org.junit.Test

class EnterActivityTest : ActivityTestBase<EnterActivity>(EnterActivity::class.java) {

    /** Check that the initial display shows the activity views. */
    @Test
    fun testInitialState() {
        // The toolbar, search name and network name edit widgets should be visible.
        checkViewVisibility(ViewMatchers.withId(R.id.searchToolbar), ViewMatchers.Visibility.VISIBLE)
        checkViewVisibility(ViewMatchers.withId(R.id.titleInput), ViewMatchers.Visibility.VISIBLE)
        checkViewVisibility(ViewMatchers.withId(R.id.networkInput), ViewMatchers.Visibility.VISIBLE)

        // The options menu item should be visible but disabled by default.
        checkViewVisibility(ViewMatchers.withId(R.id.save), ViewMatchers.Visibility.VISIBLE)
        Espresso.onView(ViewMatchers.withId(R.id.save)).check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
    }


    /** Check that the save option is correctly enabled or disabled depending on the state */
    @Test
    fun testSaveEnabling() {
        // Edit a value into the name edit text box and ensure the save button is still not enabled.
        val text = "Some video text"
        val network = "HBO Go"

        // When the title name is entered but the network is not we should not be able to save
        onView(withId(R.id.titleInput)).perform(replaceText(text))
        onView(withId(R.id.save)).check(matches(not(isEnabled())))
        onView(withId(R.id.networkInput)).perform(pressImeActionButton()).check(matches(isDisplayed()))

        // When the title is not entered but the network is entered, we should still not be able to save
        onView(withId(R.id.titleInput)).perform(replaceText(""))
        onView(withId(R.id.networkInput)).perform(replaceText(network))
        onView(withId(R.id.save)).check(matches(not(isEnabled())))
        onView(withId(R.id.networkInput)).perform(pressImeActionButton()).check(matches(isDisplayed()))

        // When both have text, we should be able to save.
        onView(withId(R.id.titleInput)).perform(replaceText(text))
        onView(withId(R.id.networkInput)).perform(replaceText(network))
        onView(withId(R.id.save)).check(matches(isEnabled())).perform(click())
//        Espresso.onView(ViewMatchers.withId(R.id.networkInput)).perform(ViewActions.pressImeActionButton())
        Assert.assertTrue(rule.activity.isFinishing)
    }

}