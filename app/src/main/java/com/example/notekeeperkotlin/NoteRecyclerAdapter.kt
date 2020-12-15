package com.example.notekeeperkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class NoteRecyclerAdapter(context: Context, private val notes: List<NoteInfo>) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textCourse: TextView = itemView.findViewById(R.id.text_course)
        val textTitle: TextView = itemView.findViewById(R.id.text_title)
        var currentPosition = -1

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val action =
                NoteListFragmentDirections.actionSecondFragmentToFirstFragment(currentPosition)
            p0!!.findNavController().navigate(action)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.textCourse.text = note.course.title
        holder.textTitle.text = note.title
        holder.currentPosition = position

    }

    override fun getItemCount(): Int {
        return notes.size
    }
}