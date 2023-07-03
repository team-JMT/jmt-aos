package org.gdsc.presentation.view.restaurantregistration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.presentation.databinding.FoodCategoryItemBinding
import org.gdsc.presentation.model.FoodCategoryItem

class FoodCategoryRecyclerAdapter(
    private val onItemSelected: (FoodCategoryItem) -> Unit
) :
    ListAdapter<FoodCategoryItem, FoodCategoryRecyclerAdapter.FoodCategoryViewHolder>(
        diffUtil
    ) {

    private var previousPosition = 0
    private var selectedItem: FoodCategoryItem? = null

    inner class FoodCategoryViewHolder(private val binding: FoodCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FoodCategoryItem) {
            binding.categoryName.text = item.categoryItem.text
            binding.root.isSelected =
                selectedItem?.categoryItem?.text == item.categoryItem.text
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FoodCategoryItem>() {
            override fun areItemsTheSame(
                oldItem: FoodCategoryItem,
                newItem: FoodCategoryItem
            ) = oldItem.categoryItem.text == newItem.categoryItem.text

            override fun areContentsTheSame(
                oldItem: FoodCategoryItem,
                newItem: FoodCategoryItem
            ) = oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodCategoryViewHolder(
            FoodCategoryItemBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodCategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            item.let {
                selectedItem = it
                onItemSelected(it)
                this.notifyItemChanged(previousPosition)
                this.notifyItemChanged(holder.absoluteAdapterPosition)
                previousPosition = holder.absoluteAdapterPosition
            }
        }
    }

}