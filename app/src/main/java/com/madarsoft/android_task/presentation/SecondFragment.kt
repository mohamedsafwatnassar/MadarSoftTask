package com.madarsoft.android_task.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.madarsoft.android_task.R
import com.madarsoft.android_task.data.mapper.PersonalDataMapper.toDateModel
import com.madarsoft.android_task.databinding.FragmentSecondBinding
import com.madarsoft.android_task.domain.model.PersonalData
import com.madarsoft.android_task.presentation.base.BaseFragment
import com.madarsoft.android_task.util.LoadingErrorState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : BaseFragment() {

    // Binding for the fragment's layout views
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!  // Non-nullable binding access

    // ViewModel for fetching personal data
    private val vm: GetPersonalDataViewModel by viewModels()

    // Variable to hold the personal ID passed as an argument to the fragment
    private var personalId: Int? = null

    // Called when the fragment's view is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment's layout and return the root view
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called when the view is created, where the fragment fetches data
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the personal ID passed to the fragment via arguments
        personalId = requireArguments().getInt("PersonalId")

        // Log the retrieved personal ID for debugging
        Log.d("SecondFragment", "PersonalId: $personalId")

        // Fetch personal data using the ViewModel and the retrieved ID
        personalId?.let {
            vm.fetchPersonalDataFromDatabase(it)
        } ?: run {
            Log.d("SecondFragment", "PersonalId is null!")
        }

        // Setup the observers for the ViewModel
        setupObservers()
    }

    // Setup observers to listen for updates to the view state and personal data
    private fun setupObservers() {
        // Observe the ViewModel's viewState to handle loading/error states
        vm.viewState.observe(viewLifecycleOwner, ::handleViewState)

        // Observe the ViewModel's personalData LiveData to update the UI with fetched data
        vm.personalData.observe(viewLifecycleOwner) { personalData ->
            // Log the personal data for debugging
            Log.d("PersonalData", "Fetched Data: $personalData")
            Log.d("PersonalData", "Mapped Data: ${personalData.toDateModel()}")

            // Update the UI by calling the function to set the data in TextViews
            updateUI(personalData)
        }
    }

    // Function to update the UI with the personal data fetched from the database
    private fun updateUI(personalData: PersonalData) {
        // Set the TextViews with the respective values
        binding.tvUsername.text = "Name: ${personalData.username}"
        binding.tvAge.text = "Age: ${personalData.age}"
        binding.tvJobTitle.text = "Job Title: ${personalData.jobTitle}"
        binding.tvGender.text = "Gender: ${personalData.gender}"
    }

    // Handle the ViewModel's state for loading, errors, and network errors
    private fun handleViewState(state: LoadingErrorState) {
        when (state) {
            LoadingErrorState.ShowLoading -> {
                showLoading()  // Show loading spinner when data is being fetched
            }

            LoadingErrorState.HideLoading -> {
                hideLoading()  // Hide loading spinner once data is fetched
            }

            is LoadingErrorState.ShowError -> {
                Log.d("ShowError", state.error.message)
                showToast(state.error.message)  // Show an error message if an issue occurs
            }

            is LoadingErrorState.ShowNetworkError -> {
                showToast(getString(R.string.network_error))  // Show network error message
            }
        }
    }

    // Clean up the binding when the view is destroyed to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
