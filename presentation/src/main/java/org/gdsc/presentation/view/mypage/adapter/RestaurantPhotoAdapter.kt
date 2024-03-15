package org.gdsc.presentation.view.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.presentation.databinding.ItemPhotoRestaurantBinding
import org.gdsc.presentation.model.RestaurantPhotoItem

class RestaurantPhotoAdapter(
    private val onItemSelected: () -> Unit
) :
    ListAdapter<RestaurantPhotoItem, RestaurantPhotoAdapter.RestaurantPhotoViewHolder>(
        diffUtil
    ) {

    private var width = 0
    private var height = 0

    inner class RestaurantPhotoViewHolder(private val binding: ItemPhotoRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantPhotoItem) {
            with(binding) {

                if (item.photoUrl != "BUTTON") {
                    Glide.with(root)
                        .load(item.photoUrl)
                        .into(ivPhoto)
                } else {
                    morePhotoButton.visibility = ViewGroup.VISIBLE
                    morePhotoButton.setOnClickListener {
                        onItemSelected()
                    }
                }

            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RestaurantPhotoItem>() {
            override fun areItemsTheSame(
                oldItem: RestaurantPhotoItem,
                newItem: RestaurantPhotoItem
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: RestaurantPhotoItem,
                newItem: RestaurantPhotoItem
            ) = oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantPhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RestaurantPhotoViewHolder(
            ItemPhotoRestaurantBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RestaurantPhotoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemSelected()
        }
        holder.itemView.layoutParams.width = width
        holder.itemView.layoutParams.height = height
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

}