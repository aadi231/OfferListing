package com.example.offerlisting.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offerlisting.databinding.FragmentOfferCategoryBinding
import com.example.offerlisting.ui.adapter.FilterListAdapter
import com.example.offerlisting.viewmodel.OffersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferCategoryFragment : Fragment() {

    private lateinit var binding : FragmentOfferCategoryBinding
    private val filterAdapter by lazy { FilterListAdapter(onChangeCategorySelection = onChangeCategorySelection) }
    private val viewModel by activityViewModels<OffersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfferCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupClickListener()
        setupObserver()

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateBack()
                }
            })
    }

    private fun setupAdapter() {
        binding.categoryRecyclerView.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObserver() {
        viewModel.selectedCategoryList.observe(viewLifecycleOwner) { categoryList ->
            categoryList?.let {
                filterAdapter.submitList(it)
            }
        }
    }

    private fun setupClickListener() {
        binding.resetTextView.setOnClickListener {
            viewModel.resetFilters()
        }

        binding.appBaraBackButton.setOnClickListener {
            navigateBack()
        }

        binding.filterApplyButton.setOnClickListener {
            viewModel.setSelectedCategories()
            navigateBack()
        }
    }

    private fun navigateBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private val onChangeCategorySelection = { selectedId : String, isChecked : Boolean ->
        viewModel.updateSelectedIdList(selectedId, checked = isChecked)
    }
}