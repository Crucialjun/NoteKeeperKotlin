package com.example.notekeeperkotlin

import android.content.*
import android.database.Cursor
import android.net.Uri

class NoteKeeperProvider : ContentProvider() {

    lateinit var dbOpenHelper: NoteKeeperOpenHelper
    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

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

        val uriMatch = uriMatcher.match(uri)

        when (uriMatch) {
            0 -> {

                cursor = db.query(
                    NoteKeeperDatabaseContract.CourseInfoEntry.TABLE_NAME,
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
                    NoteKeeperDatabaseContract.NoteInfoEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
        }


        return cursor
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}