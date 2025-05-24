package com.example.offerlisting.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.offerlisting.data.datamodel.CategoryModel
import com.example.offerlisting.databinding.FilterCategoryItemListingLayoutBinding

class FilterListAdapter(private val onChangeCategorySelection: (String, Boolean) -> Unit) :
     RecyclerView.Adapter<FilterListAdapter.FilterViewHolder>() {

    private val data = mutableListOf<CategoryModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            FilterCategoryItemListingLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    inner class FilterViewHolder(
        private val binding: FilterCategoryItemListingLayoutBinding
    ) : ViewHolder(binding.root) {

        fun bindData(categoryModel: CategoryModel) {
            binding.categoryCheckbox.apply {
                text = categoryModel.title
                isChecked = categoryModel.isSelected

                setOnCheckedChangeListener(null)
                setOnCheckedChangeListener { _, checked ->
                    onChangeCategorySelection(categoryModel.id, checked)
                }
            }
        }
    }

    fun submitList(categoryList : List<CategoryModel>) {
        data.clear()
        data.addAll(categoryList)
        notifyDataSetChanged()
    }
}