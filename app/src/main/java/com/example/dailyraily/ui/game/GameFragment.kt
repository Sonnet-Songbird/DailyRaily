package com.example.dailyraily.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyraily.databinding.FragmentGameBinding
import com.example.dailyraily.ui.list.ListAdapter

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null

    private lateinit var viewModel: GameViewModel
    private lateinit var recyclerView: RecyclerView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.frameList.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]


    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
        recyclerView.adapter = ListAdapter(viewModel.data, requireContext())
        recyclerView.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
