package com.example.notekeeperkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NoteListFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = "NoteKeeper"
       val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { p0 ->
            val action = NoteListFragmentDirections.actionSecondFragmentToFirstFragment()
            p0!!.findNavController().navigate(action)
        }

        initializeDisplayContent(view)
    }

    private fun initializeDisplayContent(view: View) {
        val notesList = view.findViewById<ListView>(R.id.list_notes)

        val notes = DataManager.getInstance().notes

        val adapterNotes =
            ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,notes)

        notesList.adapter = adapterNotes

        notesList.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                val note : NoteInfo = notesList.getItemAtPosition(position) as NoteInfo
                val action = NoteListFragmentDirections.actionSecondFragmentToFirstFragment(note)
                p1!!.findNavController().navigate(action) }
    }
}