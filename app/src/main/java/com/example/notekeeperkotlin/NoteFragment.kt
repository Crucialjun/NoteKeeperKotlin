package com.example.notekeeperkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class NoteFragment : Fragment() {

    private var position = -1
    private var isNewNote = true
    private var mNote: NoteInfo? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note, container, false)

        readDisplayStateValues()

        return view
    }

    private fun readDisplayStateValues() {
        val args = NoteFragmentArgs.fromBundle(requireArguments())
        position = args.notePosition
        isNewNote = position == -1
        if (!isNewNote) {
            mNote = DataManager.getInstance().notes[position]
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "Edit Note"


        val courses = DataManager.getInstance().courses

        val adapterCourses =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, courses)

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinnerCourses = view.findViewById<Spinner>(R.id.spinner_courses)

        spinnerCourses.adapter = adapterCourses


        val textNoteTitle = view.findViewById<TextInputEditText>(R.id.text_note_title)
        val textNoteText = view.findViewById<TextInputEditText>(R.id.text_note_text)

        if(!isNewNote){
        displayNote(spinnerCourses,textNoteTitle,textNoteText)}
    }

    private fun displayNote(
        spinnerCourses: Spinner?,
        textNoteTitle: TextInputEditText?,
        textNoteText: TextInputEditText?
    ) {

        val courses = DataManager.getInstance().courses
        val courseIndex = courses.indexOf(mNote!!.course)
        spinnerCourses!!.setSelection(courseIndex)
        textNoteTitle!!.setText(mNote!!.title)
        textNoteText!!.setText(mNote!!.text)
    }
}