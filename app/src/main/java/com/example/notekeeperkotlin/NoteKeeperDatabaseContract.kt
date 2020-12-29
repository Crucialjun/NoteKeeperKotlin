package com.example.notekeeperkotlin

import android.provider.BaseColumns

class NoteKeeperDatabaseContract {

    object CourseInfoEntry : BaseColumns {
        const val TABLE_NAME = "course_info"
        const val COLUMN_COURSE_ID = "course_id"
        const val COLUMN_COURSE_TITLE = "course_title"
        val _ID = BaseColumns._ID

        //CREATE TABLE course_info (course_id,course_title)
        val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_COURSE_ID TEXT NOT NULL, " +
                "$COLUMN_COURSE_TITLE TEXT NOT NULL)"
    }

    object NoteInfoEntry : BaseColumns {
        @kotlin.jvm.JvmField
        var _ID: String = BaseColumns._ID
        const val TABLE_NAME = "note_info"
        const val COLUMN_COURSE_ID = "course_id"
        const val COLUMN_NOTE_TITLE = "note_title"
        const val COLUMN_NOTE_TEXT = "note_text"


        //CREATE TABLE course_info (course_id,course_title)

        val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NOTE_TITLE TEXT NOT NULL," +
                "$COLUMN_NOTE_TEXT TEXT, " +
                "$COLUMN_COURSE_ID TEXT NOT NULL)"


    }
}