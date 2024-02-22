package com.example.dailyraily.ui.Input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.dailyraily.data.model.ResetType
import com.example.dailyraily.data.service.TodoListManager
import com.example.dailyraily.databinding.FragmentInputBinding
import java.time.DayOfWeek

class InputFragment : Fragment() {

    private lateinit var binding: FragmentInputBinding

    private var receivedString: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receivedString = arguments?.getString(ARG_RECEIVED_STRING)

        setupSpinnerData()
        setupButtonClick()
    }

    override fun onResume() {
        super.onResume()

        updateUIWithReceivedString()
    }


    private fun setupSpinner(spinner: Spinner, itemList: List<Any>) {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, itemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupSpinnerData() {
        val gameList = TodoListManager.getAllGame()
        setupSpinner(binding.spinnerGame, gameList)

        val dowList = DayOfWeek.entries.map { it.toString() }
        setupSpinner(binding.spinnerDayOfWeek, dowList)

        val resetTypeList = ResetType.entries.map { it.toString() }
        setupSpinner(binding.spinnerResetType, resetTypeList)
    }


    private fun setupButtonClick() {
        binding.btnRegister.setOnClickListener {
            val selectedGame = binding.spinnerGame.selectedItem.toString()
            val enteredName = binding.editTextGameName.text.toString()
            val selectedResetType = binding.spinnerResetType.selectedItem.toString()
            val enteredGoal = binding.editTextGoal.text.toString()
            val isImportant = binding.checkBoxImportant.isChecked


            updateUIWithReceivedString()
        }
    }

    private fun updateUIWithReceivedString() {
        receivedString?.let {
            setupSpinnerData()
        }
    }

    companion object {
        const val ARG_RECEIVED_STRING = "receivedString"

        fun newInstance(receivedString: String): InputFragment {
            return InputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RECEIVED_STRING, receivedString)
                }
            }
        }
    }
}
