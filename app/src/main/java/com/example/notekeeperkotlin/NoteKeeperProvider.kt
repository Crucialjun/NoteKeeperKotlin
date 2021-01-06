package com.example.notekeeperkotlin

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.example.notekeeperkotlin.NoteKeeperDatabaseContract.CourseInfoEntry
import com.example.notekeeperkotlin.NoteKeeperDatabaseContract.NoteInfoEntry

class NoteKeeperProvider : ContentProvider() {

    private lateinit var dbOpenHelper: NoteKeeperOpenHelper
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        uriMatcher.addURI(
            NoteKeeperProviderContract.AUTHORITY,
            NoteKeeperProviderContract.Courses.PATH,
            0
        )
        uriMatcher.addURI(
            NoteKeeperProviderContract.AUTHORITY,
            NoteKeeperProviderContract.Notes.PATH,
            1
        )
        uriMatcher.addURI(
            NoteKeeperProviderContract.AUTHORITY,
            NoteKeeperProviderContract.Notes.PATH_EXPANDED,
            2
        )
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        dbOpenHelper = NoteKeeperOpenHelper(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var cursor: Cursor? = null
        val db = dbOpenHelper.readableDatabase

        when (uriMatcher.match(uri)) {
            0 -> {

                cursor = db.query(
                    CourseInfoEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )

            }
            1 -> {
                cursor = db.query(
                    NoteInfoEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
            2 -> {
                cursor = notesExpandedQuery(db, projection, selection, selectionArgs, sortOrder)
            }
        }


        return cursor
    }

    private fun notesExpandedQuery(
        db: SQLiteDatabase?,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val tablesWithJoin = "${
            NoteInfoEntry.TABLE_NAME
        } JOIN " +
                "${CourseInfoEntry.TABLE_NAME} ON " +
                "${NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID)} = " +
                CourseInfoEntry.COLUMN_COURSE_ID

        return db!!.query(
            tablesWithJoin,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )


    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}