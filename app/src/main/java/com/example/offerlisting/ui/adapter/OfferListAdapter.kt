package com.example.offerlisting.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.offerlisting.R
import com.example.offerlisting.data.datamodel.OffersModel
import com.example.offerlisting.databinding.OfferListItemViewBinding

class OfferListAdapter : ListAdapter<OffersModel, OfferListAdapter.OfferViewHolder>(OfferDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = OfferListItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OfferViewHolder(private val binding: OfferListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(offer: OffersModel) {

            binding.offerItemTextView.text = offer.offerName

            Glide.with(binding.offerItemImageView.context)
                .load(offer.offerImageUrl)
                .into(binding.offerItemImageView)
        }
    }

    private class OfferDiffCallback : DiffUtil.ItemCallback<OffersModel>() {
        override fun areItemsTheSame(oldItem: OffersModel, newItem: OffersModel): Boolean {
            return oldItem.offerName == newItem.offerName
        }

        override fun areContentsTheSame(oldItem: OffersModel, newItem: OffersModel): Boolean {
            return oldItem == newItem
        }
    }
}