package com.example.notekeeperkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CoursesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courses = DataManager.getInstance().courses
        val courseRecyclerAdapter = CourseRecyclerAdapter(requireContext(), courses)
        val coursesLayoutManager = GridLayoutManager(requireContext(), 2)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_courses)
        recyclerView.layoutManager = coursesLayoutManager
        recyclerView.adapter = courseRecyclerAdapter
    }
}