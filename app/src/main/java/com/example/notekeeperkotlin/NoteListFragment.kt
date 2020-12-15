package com.example.notekeeperkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            val action = NoteListFragmentDirections.actionSecondFragmentToFirstFragment(-1)
            p0!!.findNavController().navigate(action)
        }

        initializeDisplayContent(view)
    }

    private fun initializeDisplayContent(view: View) {
//        val notesList = view.findViewById<ListView>(R.id.list_notes)
//
//        val notes = DataManager.getInstance().notes
//
//        val adapterNotes =
//            ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,notes)
//
//        notesList.adapter = adapterNotes
//
//        notesList.onItemClickListener =
//            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
//                //val note : NoteInfo = notesList.getItemAtPosition(position) as NoteInfo
//                val action = NoteListFragmentDirections.actionSecondFragmentToFirstFragment(position)
//                p1!!.findNavController().navigate(action)
//            }

        val notesList = view.findViewById<RecyclerView>(R.id.list_notes)
        val notesLayoutManager = LinearLayoutManager(context)
        notesList.layoutManager = notesLayoutManager

        val notes = DataManager.getInstance().notes
        val noteRecyclerAdapter = NoteRecyclerAdapter(requireContext(), notes)
        notesList.adapter = noteRecyclerAdapter


    }
}