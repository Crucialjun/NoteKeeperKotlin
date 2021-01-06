package com.example.notekeeperkotlin

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.example.notekeeperkotlin.NoteKeeperDatabaseContract.CourseInfoEntry
import com.example.notekeeperkotlin.NoteKeeperDatabaseContract.NoteInfoEntry
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var noteId = -1
    private var isNewNote = true
    private var mNote: NoteInfo? = null
    private var spinnerCourses: Spinner? = null
    private var textNoteTitle: TextInputEditText? = null
    private var textNoteText: TextInputEditText? = null
    private var notePosition: Int = -1
    private var isCancelling: Boolean = false
    private var viewModel: NoteFragmentViewModel? = null
    private lateinit var dbOpenHelper: NoteKeeperOpenHelper
    private lateinit var noteCursor: Cursor
    private var noteTextPos: Int = 0
    private var noteTitlePos: Int = 0
    private var courseIDPos: Int = 0
    private lateinit var adapterCourses: SimpleCursorAdapter
    private val LOADER_NOTES = 0
    private val LOADER_COURSES = 1
    private var coursesQueryFinished = false
    private var notessQueryFinished = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)

        val viewModelProvider = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )


        viewModel = viewModelProvider.get(NoteFragmentViewModel::class.java)
        if (viewModel!!.isNewlyCreated && savedInstanceState != null) {
            viewModel!!.restoreState(savedInstanceState)
        }

        viewModel!!.isNewlyCreated = false
        dbOpenHelper = NoteKeeperOpenHelper(context)
        setHasOptionsMenu(true)
        readDisplayStateValues()
        saveOriginalNoteValues()


        return view
    }

    private fun saveOriginalNoteValues() {
        if (isNewNote) {
            return
        } else {
            viewModel?.originalNoteCourseID = mNote?.course?.courseId.toString()
            viewModel?.originalNoteTitle = mNote?.title.toString()
            viewModel?.originalNoteText = mNote?.text.toString()

        }
    }

    private fun readDisplayStateValues() {
        val args = NoteFragmentArgs.fromBundle(requireArguments())
        noteId = args.notePosition
        isNewNote = noteId == -1
        if (isNewNote) {
            createNewNote()
        } else {
            //mNote = DataManager.getInstance().notes[noteId]
        }
    }

    private fun createNewNote() {
//        val dm = DataManager.getInstance()
//        notePosition = dm.createNewNote()
//        mNote = dm.notes[notePosition]

        val values = ContentValues()

        values.put(NoteInfoEntry.COLUMN_COURSE_ID, "")
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE, "")
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT, "")

        val db = dbOpenHelper.writableDatabase
        noteId =
            db.insert(NoteInfoEntry.TABLE_NAME, null, values).toInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "Edit Note"


        //val courses = DataManager.getInstance().courses


        adapterCourses = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            null,
            arrayOf(CourseInfoEntry.COLUMN_COURSE_TITLE),
            intArrayOf(android.R.id.text1),
            0
        )

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        spinnerCourses = view.findViewById(R.id.spinner_courses)

        spinnerCourses!!.adapter = adapterCourses

        loaderManager.initLoader(LOADER_COURSES, null, this)
        //loadCourseData()


        textNoteTitle = view.findViewById(R.id.text_note_title)

        textNoteText = view.findViewById(R.id.text_note_text)

        if (!isNewNote) {
            //loadNoteData()
            loaderManager.initLoader(LOADER_NOTES, null, this)
        }
    }

    private fun loadCourseData() {
        val db = dbOpenHelper.readableDatabase
        val courseColumns = arrayOf(
            CourseInfoEntry.COLUMN_COURSE_TITLE,
            CourseInfoEntry.COLUMN_COURSE_ID,
            CourseInfoEntry._ID
        )
        val cursor = db.query(
            CourseInfoEntry.TABLE_NAME, courseColumns,
            null,
            null,
            null,
            null,
            CourseInfoEntry.COLUMN_COURSE_TITLE
        )

        adapterCourses.changeCursor(cursor)


    }

    private fun loadNoteData() {
        val db = dbOpenHelper.readableDatabase

        val courseId = "android_intents"
        val titleStart = "dynamic"

        val selection = "${NoteInfoEntry._ID} = ?"

        val selectionArgs = arrayOf(noteId.toString())


        val noteColumns = arrayOf(
            NoteInfoEntry.COLUMN_COURSE_ID,
            NoteInfoEntry.COLUMN_NOTE_TITLE,
            NoteInfoEntry.COLUMN_NOTE_TEXT
        )

        noteCursor = db.query(
            NoteInfoEntry.TABLE_NAME,
            noteColumns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )




        courseIDPos =
            noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID)

        noteTitlePos =
            noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE)

        noteTextPos =
            noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT)

        noteCursor.moveToNext()
        displayNote()


    }

    private fun displayNote() {

        val courseId = noteCursor.getString(courseIDPos)
        val noteTitle = noteCursor.getString(noteTitlePos)
        val noteText = noteCursor.getString(noteTextPos)


        val courseIndex = getIndexOfCourse(courseId)
        spinnerCourses!!.setSelection(courseIndex)

        textNoteTitle!!.setText(noteTitle)
        textNoteText!!.setText(noteText)
    }

    private fun getIndexOfCourse(courseId: String?): Int {
        val cursor = adapterCourses.cursor
        val courseIdPos =
            cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID)
        var courseRow = 0

        var more = cursor.moveToFirst()
        while (more) {
            val cursorCourseId = cursor.getString(courseIdPos)
            if (cursorCourseId == courseId) {
                break
            } else {
                courseRow += 1
                more = cursor.moveToNext()
            }
        }

        return courseRow
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_send_mail -> {
                sendEmail()
                return true
            }
            R.id.action_cancel -> {
                isCancelling = true
                findNavController().popBackStack()
                return true
            }
            R.id.action_next -> {
                moveNext()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_next)
        val lastNoteIndex = DataManager.getInstance().notes.size - 1
        item.isEnabled = noteId < lastNoteIndex
        super.onPrepareOptionsMenu(menu)
    }

    private fun moveNext() {
        saveNote()


        noteId += 1
        mNote = DataManager.getInstance().notes[noteId]

        saveOriginalNoteValues()

        displayNote()
        requireActivity().invalidateOptionsMenu()
    }

    private fun sendEmail() {
        val course: CourseInfo = spinnerCourses!!.selectedItem as CourseInfo
        val subject = textNoteTitle!!.text.toString()
        val text = "Checkout what i learned in the Pluralsight course " +
                "\"${course.title}\" ${textNoteText!!.text.toString()} "

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc2822"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(intent)

    }

    override fun onPause() {
        super.onPause()
        if (isCancelling) {
            if (isNewNote) {
                //DataManager.getInstance().removeNote(notePosition)
                deleteFromSqDatabase()
            } else {
                storePreviousNotevalues()
            }
        } else {
            saveNote()
        }
    }

    private fun deleteFromSqDatabase() {
        val selection = "${NoteInfoEntry._ID} = ?"
        val selectionArgs = arrayOf(noteId.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            val db = dbOpenHelper.writableDatabase
            db.delete(NoteInfoEntry.TABLE_NAME, selection, selectionArgs)
        }

    }

    private fun storePreviousNotevalues() {
        val course = DataManager.getInstance().getCourse(viewModel!!.originalNoteCourseID)
        mNote!!.course = course
        mNote!!.title = viewModel!!.originalNoteTitle
        mNote!!.text = viewModel!!.originalNoteText

    }

    private fun saveNote() {
        //mNote?.course = spinnerCourses!!.selectedItem as CourseInfo?
        val noteTitle = textNoteTitle!!.text.toString()
        val noteText = textNoteText!!.text.toString()

        val courseId: String = selectedCourseId()

        saveNoteToSqDatabase(courseId, noteTitle, noteText)

    }

    private fun selectedCourseId(): String {
        val selectedPos = spinnerCourses!!.selectedItemPosition
        val cursor = adapterCourses.cursor
        cursor.moveToPosition(selectedPos)

        val courseIdPos =
            cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID)

        return cursor.getString(courseIdPos)
    }

    private fun saveNoteToSqDatabase(courseId: String, noteTitle: String, noteText: String) {
        val selection = "${NoteInfoEntry._ID} = ?"
        val selectionArgs = arrayOf("$noteId")

        val values = ContentValues()
        values.put(NoteInfoEntry.COLUMN_COURSE_ID, courseId)
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE, noteTitle)
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT, noteText)

        val db = dbOpenHelper.writableDatabase
        db.update(
            NoteInfoEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel!!.saveState(outState)
    }

    override fun onDestroyView() {
        dbOpenHelper.close()
        super.onDestroyView()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var loader: CursorLoader? = null
        if (id == LOADER_NOTES) {
            loader = createLoaderNotes()
        } else if (id == LOADER_COURSES) {
            loader = createLoaderCourses()
        }
        return loader as Loader<Cursor>

    }

    private fun createLoaderCourses(): CursorLoader {
        coursesQueryFinished = false

        val uri = Uri.parse("content://com.example.notekeeperkotlin.provider")

        val courseColumns = arrayOf(
            CourseInfoEntry.COLUMN_COURSE_TITLE,
            CourseInfoEntry.COLUMN_COURSE_ID,
            CourseInfoEntry._ID
        )

        return object : CursorLoader(
            requireContext(),
            uri,
            courseColumns,
            null,
            null,
            CourseInfoEntry.COLUMN_COURSE_TITLE
        ) {}


//        return object : CursorLoader(requireContext()) {
//            override fun loadInBackground(): Cursor? {
//                val db = dbOpenHelper.readableDatabase
//
//                return db.query(
//                    CourseInfoEntry.TABLE_NAME, courseColumns,
//                    null,
//                    null,
//                    null,
//                    null,
//                    CourseInfoEntry.COLUMN_COURSE_TITLE
//                )
//            }
//        }
    }

    private fun createLoaderNotes(): CursorLoader {
        notessQueryFinished = false
        return object : CursorLoader(requireContext()) {
            override fun loadInBackground(): Cursor? {
                val db = dbOpenHelper.readableDatabase

                val selection = "${NoteInfoEntry._ID} = ?"

                val selectionArgs = arrayOf(noteId.toString())


                val noteColumns = arrayOf(
                    NoteInfoEntry.COLUMN_COURSE_ID,
                    NoteInfoEntry.COLUMN_NOTE_TITLE,
                    NoteInfoEntry.COLUMN_NOTE_TEXT
                )

                return db.query(
                    NoteInfoEntry.TABLE_NAME,
                    noteColumns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
                )
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (loader.id == LOADER_NOTES) {
            loadFinishedNotes(data)
        } else if (loader.id == LOADER_COURSES) {
            adapterCourses.changeCursor(data)
            coursesQueryFinished = true
            displayNotesWhenQueryFinished()
        }
    }

    private fun loadFinishedNotes(data: Cursor?) {
        noteCursor = data!!
        courseIDPos =
            noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID)

        noteTitlePos =
            noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE)

        noteTextPos =
            noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT)

        noteCursor.moveToNext()
        notessQueryFinished = true
        displayNotesWhenQueryFinished()


    }

    private fun displayNotesWhenQueryFinished() {
        if (notessQueryFinished && coursesQueryFinished)
            displayNote()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (loader.id == LOADER_NOTES) {
            noteCursor.close()
        } else if (loader.id == LOADER_COURSES) {
            adapterCourses.changeCursor(null)
        }
    }
}