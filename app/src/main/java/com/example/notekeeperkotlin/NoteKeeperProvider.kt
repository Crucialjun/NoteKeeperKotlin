package com.example.notekeeperkotlin

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.provider.BaseColumns
import com.example.notekeeperkotlin.NoteKeeperDatabaseContract.CourseInfoEntry
import com.example.notekeeperkotlin.NoteKeeperDatabaseContract.NoteInfoEntry

class NoteKeeperProvider : ContentProvider() {

    private lateinit var dbOpenHelper: NoteKeeperOpenHelper
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        uriMatcher.addURI(
            NoteKeeperProviderContract().authority,
            NoteKeeperProviderContract().Courses().PATH,
            0
        )
        uriMatcher.addURI(
            NoteKeeperProviderContract().authority,
            NoteKeeperProviderContract().Notes().PATH,
            1
        )
        uriMatcher.addURI(
            NoteKeeperProviderContract().authority,
            NoteKeeperProviderContract().Notes().PATH_EXPANDED,
            2
        )
        uriMatcher.addURI(
            NoteKeeperProviderContract().authority,
            "${NoteKeeperProviderContract().Notes().PATH}/#",
            3
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
        val db = dbOpenHelper.writableDatabase
        var rowId: Long = -1
        var rowUri: Uri? = null

        when (uriMatcher.match(uri)) {
            1 -> {
                rowId = db.insert(NoteInfoEntry.TABLE_NAME, null, values)

                //content://com.example.notekeeperkotlin.provider/notes/1

                rowUri = ContentUris.withAppendedId(
                    NoteKeeperProviderContract().Notes().CONTENT_URI,
                    rowId
                )


            }

            0 -> {
                rowId = db.insert(CourseInfoEntry.TABLE_NAME, null, values)

                //content://com.example.notekeeperkotlin.provider/courses/1

                rowUri = ContentUris.withAppendedId(
                    NoteKeeperProviderContract().Courses().CONTENT_URI,
                    rowId
                )
            }

            2 -> {
                // Throw exceprion read only table
            }
        }

        return rowUri
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

            3 -> {
                val rowId = ContentUris.parseId(uri)
                val rowSelection = NoteInfoEntry._ID
                val rowSelectionArgs = arrayOf(rowId.toString())

                cursor = db.query(
                    NoteInfoEntry.TABLE_NAME,
                    projection,
                    rowSelection,
                    rowSelectionArgs,
                    null,
                    null,
                    null
                )
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
        val columns = arrayOfNulls<String>(projection?.size!!)

        for (x in projection.indices) {
            if (projection[x] == BaseColumns._ID ||
                projection[x] == NoteKeeperProviderContract.CoursesIdColumns.COLUMN_COURSE_ID
            ) {
                columns[x] = NoteInfoEntry.getQName(projection[x])
            } else {
                columns[x] = projection[x]
            }
        }


        val tablesWithJoin = "${
            NoteInfoEntry.TABLE_NAME
        } JOIN " +
                "${CourseInfoEntry.TABLE_NAME} ON " +
                "${NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID)} = " +
                CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID)

        return db!!.query(
            tablesWithJoin,
            columns,
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