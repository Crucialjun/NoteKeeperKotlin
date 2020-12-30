package com.example.notekeeperkotlin

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class NoteRecyclerAdapter(context: Context, private var cursor: Cursor) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var coursePos: Int = 0
    private var noteTitlePos: Int = 0
    private var idPos: Int = 0

    init {
        populateColumnPositions()
    }

    private fun populateColumnPositions() {
        //Get Columns Indexs

        coursePos = cursor.getColumnIndex(NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID)
        noteTitlePos =
            cursor.getColumnIndex(NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_NOTE_TITLE)
        idPos = cursor.getColumnIndex(NoteKeeperDatabaseContract.NoteInfoEntry._ID)
    }

    private fun changeCursor(newCursor: Cursor) {
        cursor.close()
        cursor = newCursor
        populateColumnPositions()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textCourse: TextView = itemView.findViewById(R.id.text_course)
        val textTitle: TextView = itemView.findViewById(R.id.text_title)
        var id = -1

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val action =
                NoteListFragmentDirections.actionSecondFragmentToFirstFragment(id)
            p0!!.findNavController().navigate(action)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        val course = cursor.getString(coursePos)
        val noteTitle = cursor.getString(noteTitlePos)
        val id = cursor.getInt(idPos)

        holder.textCourse.text = course
        holder.textTitle.text = noteTitle
        holder.id = id

    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}