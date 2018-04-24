package com.pajato.argusremastered

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Test

class ViewModelTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {
    /*
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
    */

    @Test
    fun testActivityResult() {
        val resultData = Intent()
        val title = "Thinger Strangs"

        resultData.putExtra(EnterActivity.TITLE_KEY, title)
        resultData.putExtra(EnterActivity.NETWORK_KEY, "Netflix")

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        Intents.intending(IntentMatchers.toPackage("com.pajato.argusremastered")).respondWith(result)
        onView(withId(R.id.fab))
                .perform(click())

        super.checkViewVisibility(withText(title), ViewMatchers.Visibility.VISIBLE)

        onView(allOf(withId(R.id.deleteButton), withParent(hasSibling(withText(title)))))
                .perform(click())

        onView(withText(title)).check(doesNotExist())
    }

    @Test fun testDelete() {
        val resultData = Intent()
        val title = "Thinger Strangs"

        resultData.putExtra(EnterActivity.TITLE_KEY, title)
        resultData.putExtra(EnterActivity.NETWORK_KEY, "Netflix")

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        Intents.intending(IntentMatchers.toPackage("com.pajato.argusremastered")).respondWith(result)
        onView(withId(R.id.fab))
                .perform(click())

        onView(allOf(withId(R.id.deleteButton), withParent(hasSibling(withText(title)))))
                .perform(click())

        onView(withText(title)).check(doesNotExist())
    }

    @Test fun testUpdate() {
        val resultData = Intent()
        val title = "Thinger Strangs"

        resultData.putExtra(EnterActivity.TITLE_KEY, title)
        resultData.putExtra(EnterActivity.NETWORK_KEY, "Netflix")

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        Intents.intending(IntentMatchers.toPackage("com.pajato.argusremastered")).respondWith(result)
        onView(withId(R.id.fab))
                .perform(click())

        onView(allOf(withId(R.id.dateButton), withParent(hasSibling(withText(title)))))
                .perform(click())

        checkViewVisibility(withText("A Date"), ViewMatchers.Visibility.VISIBLE)

        onView(allOf(withId(R.id.deleteButton), withParent(hasSibling(withText(title)))))
                .perform(click())
    }

    @Test
    fun testResult() {
        onView(withId(R.id.fab))
                .perform(click())

        checkViewVisibility(withId(R.id.searchActivityRoot), ViewMatchers.Visibility.VISIBLE)

        val text = "Luther"
        val network = "HBO Go"
        onView(withId(R.id.titleInput)).perform(replaceText(text))
        onView(withId(R.id.networkInput)).perform(replaceText(network))
        onView(withId(R.id.save)).perform(click())
    }
}