package org.gdsc.presentation.view.restaurantregistration.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.presentation.databinding.FoodCategoryItemBinding
import org.gdsc.presentation.model.FoodCategoryItem

class FoodCategoryRecyclerAdapter :
    ListAdapter<FoodCategoryItem, FoodCategoryRecyclerAdapter.FoodCategoryViewHolder>(
    diffUtil
) {

    class FoodCategoryViewHolder(private val binding: FoodCategoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FoodCategoryItem) {
            binding.categoryName.text = item.name
            binding.root.isSelected = item.isSelected
        }
    }
    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<FoodCategoryItem>() {
            override fun areItemsTheSame(
                oldItem: FoodCategoryItem,
                newItem: FoodCategoryItem
            ) = oldItem.name == newItem.name

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
            item.isSelected = item.isSelected.not()
            this.notifyItemChanged(position)
        }
    }

}