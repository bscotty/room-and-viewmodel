package com.pajato.argusremastered

import android.app.Activity
import android.content.Intent
import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Provide an abstract base class for all test classes.
 *
 * @author Paul Michael Reilly --- pmr@pajato.com
 */
@RunWith(AndroidJUnit4::class) abstract class ActivityTestBase<T : Activity>(theClass: Class<T>) {

    /** Define the component under test using a JUnit rule. */
    @Rule
    @JvmField
    val rule: ActivityTestRule<T> = IntentsTestRule(theClass)

    /** Check that a view's (via the given matcher) has the given visibility. */
    fun checkViewVisibility(viewMatcher: Matcher<View>, state: ViewMatchers.Visibility) {
        Espresso.onView(viewMatcher).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(state)))
    }

    /** Check that the next activity intent matches with the given class. */
    fun <T : Activity> nextOpenActivityIs(theClass: Class<T>) {
        Intents.intended(IntentMatchers.hasComponent(theClass.name))
    }

    /** Provide an extension on the Activity class to run code on the UI thread. */
    fun Activity.runOnUiThread(f: () -> Unit) {
        runOnUiThread { f() }
    }

    fun Activity.getIDName(id: Int): String {
        return this.resources.getResourceEntryName(id)
    }

    protected fun doLifeCycle(intent: Intent? = null) {
        rule.activity.finish()
        Intents.release()

        rule.launchActivity(intent)
    }
}
