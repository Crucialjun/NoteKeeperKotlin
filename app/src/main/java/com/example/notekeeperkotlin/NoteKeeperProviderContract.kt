package com.example.notekeeperkotlin

import android.net.Uri
import android.provider.BaseColumns

class NoteKeeperProviderContract {
    val authority: String = "com.example.notekeeperkotlin.provider"
    val authorityUri: Uri = Uri.parse("content://$authority")

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

    interface CoursesIdColumns {

        companion object {
            const val COLUMN_COURSE_ID = "course_id"

        }


    }

    inner class Courses : CourseColumns, BaseColumns, CoursesIdColumns {
        val PATH = "courses"

        //content://com.example.notekeeperkotlin.provider/course
        val CONTENT_URI: Uri = Uri.withAppendedPath(authorityUri, PATH)
        val COLUMN_COURSE_TITLE = CourseColumns.COLUMN_COURSE_TITLE
        val COLUMN_COURSE_ID = CoursesIdColumns.COLUMN_COURSE_ID
        val _ID = BaseColumns._ID
    }

    inner class Notes : NoteColumns, BaseColumns, CoursesIdColumns, CourseColumns {
        val PATH = "notes"

        //content://com.example.notekeeperkotlin.provider/notes
        val CONTENT_URI = Uri.withAppendedPath(authorityUri, PATH)
        val COLUMN_COURSE_TITLE = CourseColumns.COLUMN_COURSE_TITLE
        val COLUMN_NOTE_TITLE = NoteColumns.COLUMN_NOTE_TITLE
        val COLUMN_NOTE_TEXT = NoteColumns.COLUMN_NOTE_TEXT
        val PATH_EXPANDED = "notes_expanded"
        val CONTENT_EXPANDED_URI: Uri = Uri.withAppendedPath(authorityUri, PATH_EXPANDED)
        val _ID = BaseColumns._ID
        val COLUMN_COURSE_ID = CoursesIdColumns.COLUMN_COURSE_ID
    }


}