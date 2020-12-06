package com.example.notekeeperkotlin

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class NoteCreationTest {


    companion object {
        private lateinit var sDataManager: DataManager
        @BeforeClass
        @JvmStatic
        fun classSetUp() {
            sDataManager = DataManager.getInstance()
        }
    }

    @get:Rule
    val noteListActivityRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun testCreateNewNote() {
        val course = sDataManager.getCourse("java_lang")
        val noteTitle = "Test Note Title"
        val noteText = "This is the body of our test note"


        onView(withId(R.id.fab)).perform(click())
        onView(withId(R.id.spinner_courses)).perform(click())
        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())
        onView(withId(R.id.text_note_title))
                .perform(typeText(noteTitle))
                .check(matches((withText(containsString(noteTitle)))))
        onView(withId(R.id.text_note_text)).perform(typeText(noteText), closeSoftKeyboard())
        onView(withId(R.id.text_note_text)).check(matches((withText(containsString(noteText)))))
        pressBack()

        val noteIndex = sDataManager.notes.size - 1
        val note = sDataManager.notes[noteIndex]
        assertEquals(noteTitle, note.title)
        assertEquals(noteText, note.text)
        assertEquals(course, note.course)

    }
}