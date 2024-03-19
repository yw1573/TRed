package com.yw1573.tred.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yw1573.tred.MainActivity
import com.yw1573.tred.adapter.TableViewAdapter
import com.yw1573.tred.databinding.FragmentTableBinding

class TableFragment : Fragment() {

    private var _binding: FragmentTableBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTableBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val dbHelper = MainActivity.dbHelper
        val bloodSugarList = dbHelper!!.query(false)
        val recyclerViewMovieList = binding.tableRecyclerView
        recyclerViewMovieList.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewMovieList.adapter = TableViewAdapter(bloodSugarList)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



