package com.example.notekeeperkotlin

import android.net.Uri
import android.provider.BaseColumns

object NoteKeeperProviderContract {
    const val AUTHORITY = "com.example.notekeeperkotlin.provider"
    val AUTHORITY_URI: Uri = Uri.parse("content://$AUTHORITY")

    interface CourseColumns {

        companion object {
            const val COLUMN_COURSE_TITLE = "course_title"
        }


    }

    interface NoteColumns {

        companion object {
            const val COLUMN_NOTE_TITLE = "note_title"
            const val COLUMN_NOTE_TEXT = "note_text"
        }


    }

    interface Columns_Course_Id {

        companion object {
            const val COLUMN_COURSE_ID = "course_id"

        }


    }

    object Courses : CourseColumns, BaseColumns, Columns_Course_Id {
        const val PATH = "courses"

        //content://com.example.notekeeperkotlin.provider/course
        val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH)
    }

    object Notes : NoteColumns, BaseColumns, Columns_Course_Id, CourseColumns {
        const val PATH = "notes"

        //content://com.example.notekeeperkotlin.provider/course
        val CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH)
        const val COLUMN_COURSE_TITLE = CourseColumns.COLUMN_COURSE_TITLE
        const val COLUMN_NOTE_TITLE = NoteColumns.COLUMN_NOTE_TITLE
        const val PATH_EXPANDED = "notes_expanded"
        val CONTENT_EXPANDED_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH_EXPANDED)
    }


}