package com.example.notekeeperkotlin

import android.database.Cursor
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class NoteListFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var dbOpenHelper: NoteKeeperOpenHelper
    private lateinit var noteRecyclerAdapter: NoteRecyclerAdapter
    private var noteCursor: Cursor? = null
    private lateinit var notesList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesList = view.findViewById(R.id.list_notes)
        requireActivity().actionBar?.setDisplayShowCustomEnabled(false)
        requireActivity().title = "NoteKeeper"
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { p0 ->
            val action = NoteListFragmentDirections.actionSecondFragmentToFirstFragment(-1)
            p0!!.findNavController().navigate(action)
        }


        dbOpenHelper = NoteKeeperOpenHelper(context)

        loaderManager.initLoader(2, null, this)

        //initializeDisplayContent(view)

        val notesLayoutManager = LinearLayoutManager(context)
        notesList.layoutManager = notesLayoutManager
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

        val notesLayoutManager = LinearLayoutManager(context)
        notesList.layoutManager = notesLayoutManager


    }

    override fun onDestroyView() {
        dbOpenHelper.close()
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(2, null, this)

    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        var loader: CursorLoader? = null
        if (id == 2) {
            //val db = dbOpenHelper.readableDatabase

            val noteColumns = arrayOf(
                NoteKeeperProviderContract().Notes()._ID,
                NoteKeeperProviderContract().Notes().COLUMN_NOTE_TITLE,
                NoteKeeperProviderContract().Notes().COLUMN_COURSE_TITLE

            )


            val notesOrderBy =
                NoteKeeperProviderContract().Courses().COLUMN_COURSE_TITLE +
                        ",${NoteKeeperProviderContract().Notes().COLUMN_NOTE_TITLE}"



            loader = CursorLoader(
                requireContext(),
                NoteKeeperProviderContract().Notes().CONTENT_EXPANDED_URI,
                noteColumns,
                null,
                null,
                notesOrderBy
            )
        }

        return loader as CursorLoader
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (loader.id == 2) {
            noteRecyclerAdapter = NoteRecyclerAdapter(requireContext(), data)
            notesList.adapter = noteRecyclerAdapter
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (loader.id == 2) {
            noteRecyclerAdapter.changeCursor(null)
        }
    }
}