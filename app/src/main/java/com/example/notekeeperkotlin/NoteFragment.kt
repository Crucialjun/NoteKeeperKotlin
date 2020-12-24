package com.example.notekeeperkotlin

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteFragment : Fragment() {

    private var position = -1
    private var isNewNote = true
    private var mNote: NoteInfo? = null
    private var spinnerCourses: Spinner? = null
    private var textNoteTitle: TextInputEditText? = null
    private var textNoteText: TextInputEditText? = null
    private var notePosition: Int = -1
    private var isCancelling: Boolean = false
    private var viewModel: NoteFragmentViewModel? = null
    lateinit var dbOpenHelper: NoteKeeperOpenHelper

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
        setHasOptionsMenu(true)
        readDisplayStateValues()
        saveOriginalNoteValues()

        return view
    }

    private fun saveOriginalNoteValues() {
        if (isNewNote) {
            return
        } else {
            viewModel!!.originalNoteCourseID = mNote!!.course.courseId
            viewModel!!.originalNoteTitle = mNote!!.title
            viewModel!!.originalNoteText = mNote!!.text

        }
    }

    private fun readDisplayStateValues() {
        val args = NoteFragmentArgs.fromBundle(requireArguments())
        position = args.notePosition
        isNewNote = position == -1
        if (isNewNote) {
            createNewNote()
        } else {
            mNote = DataManager.getInstance().notes[position]
        }
    }

    private fun createNewNote() {
        val dm = DataManager.getInstance()
        notePosition = dm.createNewNote()
        mNote = dm.notes[notePosition]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "Edit Note"

        dbOpenHelper = NoteKeeperOpenHelper(context)

        val courses = DataManager.getInstance().courses

        val adapterCourses =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses)

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        spinnerCourses = view.findViewById(R.id.spinner_courses)

        spinnerCourses!!.adapter = adapterCourses



        textNoteTitle = view.findViewById(R.id.text_note_title)

        textNoteText = view.findViewById(R.id.text_note_text)

        if (!isNewNote) {
            loadNoteData()
        }
    }

    private fun loadNoteData() {
        val db = dbOpenHelper.readableDatabase


    }

    private fun displayNote() {

        val courses = DataManager.getInstance().courses
        val courseIndex = courses.indexOf(mNote!!.course)
        spinnerCourses!!.setSelection(courseIndex)
        textNoteTitle!!.setText(mNote!!.title)
        textNoteText!!.setText(mNote!!.text)
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
        item.isEnabled = position < lastNoteIndex
        super.onPrepareOptionsMenu(menu)
    }

    private fun moveNext() {
        saveNote()


        position += 1
        mNote = DataManager.getInstance().notes[position]

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
                DataManager.getInstance().removeNote(notePosition)
            } else {
                storePreviousNotevalues()
            }
        } else {
            saveNote()
        }
    }

    private fun storePreviousNotevalues() {
        val course = DataManager.getInstance().getCourse(viewModel!!.originalNoteCourseID)
        mNote!!.course = course
        mNote!!.title = viewModel!!.originalNoteTitle
        mNote!!.text = viewModel!!.originalNoteText

    }

    private fun saveNote() {
        mNote!!.course = spinnerCourses!!.selectedItem as CourseInfo?
        mNote!!.title = textNoteTitle!!.text.toString()
        mNote!!.text = textNoteText!!.text.toString()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        viewModel!!.saveState(outState)
    }

    override fun onDestroyView() {
        dbOpenHelper.close()
        super.onDestroyView()
    }
}