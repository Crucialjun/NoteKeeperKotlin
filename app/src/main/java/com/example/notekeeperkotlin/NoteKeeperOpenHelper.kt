package com.example.notekeeperkotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteKeeperOpenHelper(
    context: Context?,
    DATABASE_NAME: String = "NoteKeeper.db",
    DATABASE_VERSION: Int = 1
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL(NoteKeeperDatabaseContract.CourseInfoEntry.SQL_CREATE_TABLE)
        p0.execSQL(NoteKeeperDatabaseContract.NoteInfoEntry.SQL_CREATE_TABLE)

        val worker = DatabaseDataWorker(p0)
        worker.insertCourses()
        worker.insertSampleNotes()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}