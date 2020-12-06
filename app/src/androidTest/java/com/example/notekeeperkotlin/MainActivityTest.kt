package com.example.notekeeperkotlin


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                1),
                        isDisplayed()))
        floatingActionButton.perform(click())

        val textInputEditText = onView(
                allOf(withId(R.id.text_note_title),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.textInputLayout),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText.perform(click())

        val textInputEditText2 = onView(
                allOf(withId(R.id.text_note_title),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.textInputLayout),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText2.perform(replaceText("Note Title"), closeSoftKeyboard())

        val textInputEditText3 = onView(
                allOf(withId(R.id.text_note_text),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText3.perform(replaceText("This is my sampleo x"), closeSoftKeyboard())

        val textInputEditText4 = onView(
                allOf(withId(R.id.text_note_text), withText("This is my sampleo x"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText4.perform(click())

        val textInputEditText5 = onView(
                allOf(withId(R.id.text_note_text), withText("This is my sampleo x"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText5.perform(replaceText("This is my sample"))

        val textInputEditText6 = onView(
                allOf(withId(R.id.text_note_text), withText("This is my sample"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        textInputEditText6.perform(closeSoftKeyboard())
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
