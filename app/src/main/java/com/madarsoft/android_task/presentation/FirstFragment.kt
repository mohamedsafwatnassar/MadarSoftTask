package com.madarsoft.android_task.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.madarsoft.android_task.data.local.entity.PersonalDataEntity
import com.madarsoft.android_task.databinding.FragmentFirstBinding
import com.madarsoft.android_task.presentation.base.BaseFragment
import com.madarsoft.android_task.presentation.viewmodel.AddPersonalDataViewModel
import com.madarsoft.android_task.util.DataState
import com.madarsoft.android_task.util.LoadingErrorState
import com.madarsoft.android_task.util.extention.onDebouncedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : BaseFragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val addPersonalDataViewModel: AddPersonalDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnSave.onDebouncedListener {
            addPersonalDataViewModel.addPersonalDataToDatabase(
                PersonalDataEntity(
                    id = 0,
                    username = "Mohamed 1",
                    age = "24",
                    jobTitle = "Engineer",
                    gender = "Male"
                )
            )
        }
    }

    private fun setupObservers() {
        addPersonalDataViewModel.viewState.observe(
            viewLifecycleOwner,
            ::handleViewState
        )  // Observe loading/error states
        addPersonalDataViewModel.personalData.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Success -> {
                    Log.d("PersonalData", "Personal Data $it")
                }

                is DataState.Error -> {
                    // Handle error state, post the error message to _viewState
                }

                DataState.Idle -> {
                    // Hide loading state when idle (no data loading in progress)
                    hideLoading()
                }

                DataState.Processing -> {
                    // Show loading state when data is being processed
                    showLoading()
                }

                DataState.ServerError -> {
                    // Handle server error state if necessary (show a network error)
                }
            }
        }
    }

    private fun handleViewState(state: LoadingErrorState) {
        when (state) {
            LoadingErrorState.ShowLoading -> showLoading()  // Show loading spinner
            LoadingErrorState.HideLoading -> hideLoading()  // Hide loading spinner
            is LoadingErrorState.ShowError -> showToast(state.error.message)  // Show error message
            is LoadingErrorState.ShowNetworkError -> showToast("Network error")  // Show network error message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}