package com.example.offerlisting.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offerlisting.AppConstants
import com.example.offerlisting.R
import com.example.offerlisting.databinding.FragmentOfferListingBinding
import com.example.offerlisting.ui.adapter.OfferListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OfferListingFragment : Fragment() {

    private lateinit var binding : FragmentOfferListingBinding
    private val offerAdapter by lazy { OfferListAdapter() }

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
    }

    private fun setupAdapter() {
        binding.offerRecyclerView.apply {
            adapter = offerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListener() {
        binding.filterText.setOnClickListener {
            AppConstants.navigateToTargetFragmentWithBackStack(requireActivity(), OfferCategoryFragment())
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}