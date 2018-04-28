package com.pajato.argusremastered

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import java.text.DateFormat
import java.util.*

class ViewModelTest : ActivityTestBase<MainActivity>(MainActivity::class.java) {
    @Test
    fun testActivityResult() {
        val resultData = Intent()
        val title = "Thinger Strangs"
        val network = "Netflix"

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

    @Test
    fun testDelete() {
        val resultData = Intent()
        val title = "Thinger Strangs"
        val network = "Netflix"

        resultData.putExtra(EnterActivity.TITLE_KEY, title)
        resultData.putExtra(EnterActivity.NETWORK_KEY, network)

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        Intents.intending(IntentMatchers.toPackage("com.pajato.argusremastered")).respondWith(result)
        onView(withId(R.id.fab))
                .perform(click())

        onView(allOf(withId(R.id.deleteButton), withParent(hasSibling(withText(title)))))
                .perform(click())

        onView(withText(title)).check(doesNotExist())
    }

    @Test
    fun testUpdate() {
        val resultData = Intent()
        val title = "Thinger Strangs"
        val network = "Netflix"
        val date = DateFormat.getDateInstance().format(Date())

        resultData.putExtra(EnterActivity.TITLE_KEY, title)
        resultData.putExtra(EnterActivity.NETWORK_KEY, network)

        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        Intents.intending(IntentMatchers.toPackage("com.pajato.argusremastered")).respondWith(result)
        onView(withId(R.id.fab))
                .perform(click())

        onView(allOf(withId(R.id.dateButton), withParent(hasSibling(withText(title)))))
                .perform(click())
        super.respondToPermission(false)
        checkViewVisibility(withText(date), ViewMatchers.Visibility.VISIBLE)

        onView(allOf(withId(R.id.dateButton), withParent(hasSibling(withText(title)))))
                .perform(click())
        super.respondToPermission(true)

        checkViewVisibility(withId(R.id.locationText), ViewMatchers.Visibility.VISIBLE)
        onView(withId(R.id.locationText))
                .check(matches(withText(not(""))))

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