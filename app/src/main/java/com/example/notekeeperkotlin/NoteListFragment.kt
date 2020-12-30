package com.example.notekeeperkotlin

import android.database.Cursor
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

    private lateinit var dbOpenHelper: NoteKeeperOpenHelper
    lateinit var noteRecyclerAdapter: NoteRecyclerAdapter
    var noteCursor: Cursor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().actionBar?.setDisplayShowCustomEnabled(false)
        requireActivity().title = "NoteKeeper"
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { p0 ->
            val action = NoteListFragmentDirections.actionSecondFragmentToFirstFragment(-1)
            p0!!.findNavController().navigate(action)
        }


        dbOpenHelper = NoteKeeperOpenHelper(context)

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

        DataManager.loadFromDatabase(dbOpenHelper)
        val notesList = view.findViewById<RecyclerView>(R.id.list_notes)
        val notesLayoutManager = LinearLayoutManager(context)
        notesList.layoutManager = notesLayoutManager


        val db = dbOpenHelper.readableDatabase

        val noteColumns = arrayOf(
            NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE,
            NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID,
            NoteKeeperDatabaseContract.NoteInfoEntry._ID
        )


        noteCursor = db.query(
            NoteKeeperDatabaseContract.NoteInfoEntry.TABLE_NAME,
            noteColumns,
            null,
            null,
            null,
            null,
            NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID
                    + "," + NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE
        )

        noteRecyclerAdapter = NoteRecyclerAdapter(requireContext(), noteCursor)
        notesList.adapter = noteRecyclerAdapter


    }

    override fun onDestroyView() {
        dbOpenHelper.close()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        loadNotes()
    }

    private fun loadNotes() {
        val db = dbOpenHelper.readableDatabase

        val noteColumns = arrayOf(
            NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE,
            NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID,
            NoteKeeperDatabaseContract.NoteInfoEntry._ID
        )

        val noteCursor = db.query(
            NoteKeeperDatabaseContract.NoteInfoEntry.TABLE_NAME,
            noteColumns,
            null,
            null,
            null,
            null,
            NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID
                    + "," + NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE
        )

        noteRecyclerAdapter.changeCursor(noteCursor)


    }
}