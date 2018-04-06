package com.pajato.argusremastered

import android.arch.lifecycle.ViewModelProviders
import android.content.pm.ActivityInfo
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.pajato.argusremastered.viewmodel.MainActivityViewModel
import junit.framework.Assert.assertTrue
import org.hamcrest.Matchers.allOf
import org.junit.Test

class ViewModelTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {
    @Test
    fun testUi() {
        val initialValue = 0
        val secondValue = 1
        val vm = ViewModelProviders.of(rule.activity).get(MainActivityViewModel::class.java)

        assertTrue(vm.count.value == initialValue)
        onView(withId(R.id.counter))
                .check(matches(withText("" + initialValue)))

        onView(withId(R.id.fab))
                .perform(click())

        assertTrue(vm.count.value == secondValue)
        onView(withId(R.id.counter))
                .check(matches(withText("" + secondValue)))

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("1" + rule.activity.objLoaded)))
                .check(matches(isDisplayed()));

        // Ensure an orientation change does not destroy the view model.
        rule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        rule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        assertTrue(vm.count.value == secondValue)
        onView(withId(R.id.counter))
                .check(matches(withText("" + secondValue)))

        onView(withId(R.id.fab))
                .perform(click())
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("2" + rule.activity.objLoaded)))
                .check(matches(isDisplayed()));

    }
}