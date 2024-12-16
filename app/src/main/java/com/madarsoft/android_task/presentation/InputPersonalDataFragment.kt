package com.madarsoft.android_task.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.madarsoft.android_task.R
import com.madarsoft.android_task.data.mapper.PersonalDataMapper.toDateModel
import com.madarsoft.android_task.databinding.FragmentInputPersonalDataBinding
import com.madarsoft.android_task.presentation.base.BaseFragment
import com.madarsoft.android_task.presentation.viewmodel.AddPersonalDataViewModel
import com.madarsoft.android_task.util.LoadingErrorState
import com.madarsoft.android_task.util.extention.customNavigate
import com.madarsoft.android_task.util.extention.onDebouncedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputPersonalDataFragment : BaseFragment() {

    private var _binding: FragmentInputPersonalDataBinding? = null
    private val binding get() = _binding!!

    private val addPersonalDataViewModel: AddPersonalDataViewModel by viewModels()

    private lateinit var username: String
    private lateinit var age: String
    private lateinit var jobTitle: String
    private lateinit var gender: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnSubmit.onDebouncedListener {
            if (validate()) {
                addPersonalDataViewModel.addPersonalDataToDatabase(
                    username, age, jobTitle, gender
                )
            }
        }
    }

    private fun setupObservers() {
        addPersonalDataViewModel.viewState.observe(
            viewLifecycleOwner,
            ::handleViewState
        )  // Observe loading/error states
        addPersonalDataViewModel.personalData.observe(viewLifecycleOwner) {
            val bundle = Bundle()
            bundle.putInt("PersonalId", it.toDateModel().id)
            findNavController().customNavigate(R.id.SecondFragment, bundle)
        }
    }

    private fun handleViewState(state: LoadingErrorState) {
        when (state) {
            LoadingErrorState.ShowLoading -> showLoading()  // Show loading spinner
            LoadingErrorState.HideLoading -> hideLoading()  // Hide loading spinner
            is LoadingErrorState.ShowError -> showToast(state.error.message)  // Show error message
            is LoadingErrorState.ShowNetworkError -> showToast(getString(R.string.network_error))  // Show network error message
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        username = binding.edtUsername.text.toString().trim()
        age = binding.edtAge.text.toString().trim()
        jobTitle = binding.edtJobTitle.text.toString().trim()

        val selectedGenderId = binding.genderRadioGroup.checkedRadioButtonId
        val selectedRadioButton =
            binding.genderRadioGroup.findViewById<RadioButton>(selectedGenderId)
        gender = selectedRadioButton?.text.toString()


        if (username.isEmpty()) {
            binding.edtUsername.error = getString(R.string.required)
            isValid = false
        }
        if (age.isEmpty()) {
            binding.edtAge.error = getString(R.string.required)
            isValid = false
        }
        if (jobTitle.isEmpty()) {
            binding.edtJobTitle.error = getString(R.string.required)
            isValid = false
        }

        if (selectedGenderId == -1) {
            showToast(getString(R.string.please_select_your_gender))
            isValid = false
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}