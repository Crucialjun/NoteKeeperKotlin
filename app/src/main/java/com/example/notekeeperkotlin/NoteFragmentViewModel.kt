package com.example.notekeeperkotlin

import android.os.Bundle
import androidx.lifecycle.ViewModel

class NoteFragmentViewModel : ViewModel() {

    companion object {
        const val ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeperkotlin.ORIGINAL_NOTE_COURSE_ID"
        const val ORIGINAL_NOTE_TITLE = "com.example.notekeeperkotlin.ORIGINAL_NOTE_TITLE"
        const val ORIGINAL_NOTE_TEXT = "com.example.notekeeperkotlin.ORIGINAL_NOTE_TEXT"
    }

    lateinit var originalNoteCourseID: String
    lateinit var originalNoteTitle: String
    lateinit var originalNoteText: String
    var isNewlyCreated = true


    fun saveState(outState: Bundle) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, originalNoteCourseID)
        outState.putString(ORIGINAL_NOTE_TITLE, originalNoteTitle)
        outState.putString(ORIGINAL_NOTE_TEXT, originalNoteText)
    }

    fun restoreState(instate: Bundle) {
        originalNoteCourseID = instate.getString(ORIGINAL_NOTE_COURSE_ID)!!
        originalNoteTitle = instate.getString(ORIGINAL_NOTE_TITLE)!!
        originalNoteText = instate.getString(ORIGINAL_NOTE_TEXT)!!
    }


}