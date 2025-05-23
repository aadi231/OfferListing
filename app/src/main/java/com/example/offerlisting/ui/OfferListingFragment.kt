package com.example.offerlisting.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offerlisting.AppConstants
import com.example.offerlisting.data.datamodel.OffersModel
import com.example.offerlisting.databinding.FragmentOfferListingBinding
import com.example.offerlisting.ui.adapter.OfferListAdapter
import com.example.offerlisting.viewmodel.OffersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferListingFragment : Fragment() {

    private lateinit var binding : FragmentOfferListingBinding
    private val offerAdapter by lazy { OfferListAdapter() }
    private val viewModel by activityViewModels<OffersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfferListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupAdapter()
        setupObserver()
        getOfferData()
    }

    private fun getOfferData() {
        viewModel.getOrderData()
    }

    private fun setupObserver() {
        viewModel.filteredOfferList.observe(viewLifecycleOwner) { offerList ->
            offerList?.let {
                binding.progressBarLayout.visibility = View.GONE
                populateRecyclerView(it)
            }
        }
    }

    private fun populateRecyclerView(offersModels: List<OffersModel>) {
        offerAdapter.submitList(offersModels)
    }

    private fun setupAdapter() {
        binding.offerRecyclerView.apply {
            adapter = offerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListener() {
        // navigate to filter screen
        binding.filterText.setOnClickListener {
            AppConstants.navigateToTargetFragmentWithBackStack(requireActivity(), OfferCategoryFragment())
        }

        // on search filter the data
        binding.searchEditText.doAfterTextChanged {
            viewModel.setSearchQuery(it.toString())
        }
    }
}