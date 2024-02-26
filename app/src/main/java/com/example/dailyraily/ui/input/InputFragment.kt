package com.example.dailyraily.ui.input

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.dailyraily.MainActivity
import com.example.dailyraily.R
import com.example.dailyraily.data.model.ResetType
import com.example.dailyraily.data.model.Todo
import com.example.dailyraily.data.service.TodoListManager
import com.example.dailyraily.databinding.FragmentInputBinding
import java.time.DayOfWeek


class InputFragment : Fragment() {

    private var _binding: FragmentInputBinding? = null
    private lateinit var viewModel: InputViewModel
    private lateinit var navController: NavController
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navController =
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[InputViewModel::class.java]


        initSpinner()
        setupButtonClick()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)!!.hideFab()

        if (updateByArgument()) {
            return
        }
    }

    private fun checkGame(): Boolean {
        if (TodoListManager.getAllGame().isEmpty()) {
            exceptionReturn("먼저 게임을 등록 해 주세요.")
            return false
        }
        return true
    }

    private fun updateByArgument(): Boolean {
        val selectedTab = arguments?.getString("selectedTab")
        val itemId = arguments?.getString("itemId")
        val isEdit: Boolean = !(itemId.isNullOrBlank())
        if (selectedTab != "Game" && !checkGame()) {
            return true
        }
        when (selectedTab) {
            "Todo", "Filter" -> {
                binding.TodoInputContainer.visibility = View.VISIBLE
                binding.GameInputContainer.visibility = View.GONE
                binding.checkBoxImportant.visibility = View.VISIBLE
                updateGameSpinner()
            }

            "Game" -> {
                binding.GameInputContainer.visibility = View.VISIBLE
                binding.TodoInputContainer.visibility = View.GONE
                binding.checkBoxImportant.visibility = View.GONE

            }

            else -> {
                exceptionReturn("매개 변수가 잘못 되었습니다.")
                return false
            }
        }

        if (isEdit) {
            when (selectedTab) {
                "Todo", "Filter" -> {
                }

                "Game" -> {
                    binding.editTextGameName.focusable = View.NOT_FOCUSABLE
                }

                else -> {
                    exceptionReturn("매개 변수가 잘못 되었습니다.")
                    return false
                }
            }
        }

        updateUiForEditByItemId()


        return true
    }

    private fun updateUiForEditByItemId() {
        val selectedTab = arguments?.getString("ItemId") ?: return
    }


    private fun setSpinnerData(spinner: Spinner, itemList: List<Any>) {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, itemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun initSpinner() {
        val dowList = DayOfWeek.entries.map { it.toString() }
        setSpinnerData(binding.spinnerDayOfWeek, dowList)

        val resetTypeList = ResetType.entries.map { it.toString() }
        setSpinnerData(binding.spinnerResetType, resetTypeList)
    }

    private fun updateGameSpinner() {
        val gameList = TodoListManager.getAllGame()
        setSpinnerData(binding.spinnerGame, gameList)
    }


    private fun setupButtonClick() {
        binding.btnRegister.setOnClickListener {
            val itemId = arguments?.getString("itemId")
            val isEdit: Boolean = !(itemId.isNullOrBlank())
            val context: Context = requireContext()
            val toast = Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT)

            when (arguments?.getString("selectedTab")) {
                "Todo", "Filter" -> {
                    val selectedGame = binding.spinnerGame.selectedItem.toString()
                    val enteredName = binding.editTextGameName.text.toString()
                    val selectedResetType = binding.spinnerResetType.selectedItem.toString()
                    val enteredGoal = binding.editTextGoal.text.toString()
                    val isImportant = binding.checkBoxImportant.isChecked.toString()
                    if ((enteredGoal.toIntOrNull() ?: 1) !in 1..99) {
                        toast.setText("횟수는 1에서 99 사이여야 합니다.. ")
                        toast.show()
                        return@setOnClickListener
                    }

                    if (enteredName.isBlank()) {
                        toast.setText("이름은 비어 있을 수 없습니다.")
                        toast.show()
                        return@setOnClickListener
                    }

                    viewModel.register(
                        isEdit, context,
                        TodoInputDTO(
                            selectedGame,
                            enteredName,
                            selectedResetType,
                            enteredGoal,
                            isImportant,
                            itemId ?: ""
                        )
                    )
                    navController.navigate(R.id.nav_todo)
                }

                "Game" -> {
                    val enteredName = binding.editTextTodoName.text.toString()
                    val selectedDayOfWeek = binding.spinnerDayOfWeek.selectedItem.toString()
                    val enteredTime = binding.editTextTime.text.toString()
                    val enteredDate = binding.editTextDateOfMonth.text.toString()



                    if ((enteredTime.toIntOrNull() ?: 0) !in 0..23) {
                        toast.setText("시간은 0에서 23 사이여야 합니다.. ")
                        toast.show()
                        return@setOnClickListener
                    }
                    if ((enteredDate.toIntOrNull() ?: 1) !in 1..28) {
                        toast.setText("날짜는 0에서 28 사이여야 합니다.. ")
                        toast.show()
                        return@setOnClickListener
                    }
                    if (enteredName.isBlank()) {
                        toast.setText("이름은 비어 있을 수 없습니다.")
                        toast.show()
                        return@setOnClickListener
                    }
                    viewModel.register(
                        isEdit, context,
                        GameInputDTO(
                            enteredName,
                            selectedDayOfWeek,
                            enteredTime,
                            enteredDate
                        )
                    )
                    navController.navigate(R.id.nav_game)
                }


                else -> {
                    exceptionReturn("매개 변수가 잘못 되었습니다.")
                    return@setOnClickListener
                }
            }
        }


    }

    //Todo 나중에 ExceptionHandler로 통합해야함.
    fun exceptionReturn(toastMsg: String) {
        Toast.makeText(requireContext(), toastMsg, Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity?)!!.showFab()

        clearViews()
        clearArgument()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearViews() {
        binding.editTextGameName.text.clear()
        binding.editTextTodoName.text.clear()
        binding.editTextGoal.text.clear()
        binding.editTextTime.text.clear()
        binding.editTextDateOfMonth.text.clear()

        // Clear spinners
        binding.spinnerGame.setSelection(0)
        binding.spinnerResetType.setSelection(0)
        binding.spinnerDayOfWeek.setSelection(0)

        // Clear checkbox
        binding.checkBoxImportant.isChecked = false
    }

    private fun clearArgument() {
        arguments?.clear()
    }


}
