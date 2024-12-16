package com.madarsoft.android_task.presentation.inputPersonalData.view

import android.os.Bundle
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
import com.madarsoft.android_task.presentation.inputPersonalData.viewmodel.AddPersonalDataViewModel
import com.madarsoft.android_task.util.LoadingErrorState
import com.madarsoft.android_task.util.extention.customNavigate
import com.madarsoft.android_task.util.extention.onDebouncedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputPersonalDataFragment : BaseFragment() {

    // View Binding for accessing views in the layout
    private var _binding: FragmentInputPersonalDataBinding? = null
    private val binding get() = _binding!!

    // ViewModel for adding personal data
    private val addPersonalDataViewModel: AddPersonalDataViewModel by viewModels()

    // Variables to store user input
    private lateinit var username: String
    private lateinit var age: String
    private lateinit var jobTitle: String
    private lateinit var gender: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and return the root view
        _binding = FragmentInputPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up listeners for button clicks
        setupListeners()

        // Observe LiveData from the ViewModel
        setupObservers()
    }

    /**
     * Sets up click listeners for views
     */
    private fun setupListeners() {
        // Handle submit button click with debounced listener to prevent rapid clicks
        binding.btnSubmit.onDebouncedListener {
            // Validate input and add personal data to the database if valid
            if (validate()) {
                addPersonalDataViewModel.addPersonalDataToDatabase(
                    username, age, jobTitle, gender
                )
            }
        }
    }

    /**
     * Observes LiveData from the ViewModel
     */
    private fun setupObservers() {
        // Observe ViewState to handle loading and error states
        addPersonalDataViewModel.viewState.observe(viewLifecycleOwner, ::handleViewState)

        // Observe personalData to navigate to the second fragment upon successful data addition
        addPersonalDataViewModel.personalData.observe(viewLifecycleOwner) { personalData ->
            val bundle = Bundle().apply {
                putInt(
                    "PersonalId",
                    personalData.toDateModel().id
                )  // Pass Personal ID to the next fragment
            }
            findNavController().customNavigate(
                R.id.SecondFragment,
                bundle
            )  // Navigate to the second fragment
        }
    }

    /**
     * Handles loading, error, and network error states
     */
    private fun handleViewState(state: LoadingErrorState) {
        when (state) {
            LoadingErrorState.ShowLoading -> showLoading()  // Show loading spinner when data is being processed
            LoadingErrorState.HideLoading -> hideLoading()  // Hide loading spinner when processing is complete
            is LoadingErrorState.ShowError -> showToast(state.error.message)  // Show an error message if something goes wrong
            is LoadingErrorState.ShowNetworkError -> showToast(getString(R.string.network_error))  // Show network error message

        }
    }

    /**
     * Validates the input fields
     * @return true if all inputs are valid, false otherwise
     */
    private fun validate(): Boolean {
        var isValid = true

        // Retrieve and trim inputs
        username = binding.edtUsername.text.toString().trim()
        age = binding.edtAge.text.toString().trim()
        jobTitle = binding.edtJobTitle.text.toString().trim()

        // Get selected gender
        val selectedGenderId = binding.genderRadioGroup.checkedRadioButtonId
        val selectedRadioButton =
            binding.genderRadioGroup.findViewById<RadioButton>(selectedGenderId)
        gender = selectedRadioButton?.text.toString()

        // Validate username
        if (username.isEmpty()) {
            binding.edtUsername.error = getString(R.string.required)
            isValid = false
        }

        // Validate age
        if (age.isEmpty()) {
            binding.edtAge.error = getString(R.string.required)
            isValid = false
        }

        // Validate job title
        if (jobTitle.isEmpty()) {
            binding.edtJobTitle.error = getString(R.string.required)
            isValid = false
        }

        // Validate gender
        if (selectedGenderId == -1) {
            showToast(getString(R.string.please_select_your_gender))
            isValid = false
        }

        return isValid
    }

    /**
     * Cleans up the binding to prevent memory leaks
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
