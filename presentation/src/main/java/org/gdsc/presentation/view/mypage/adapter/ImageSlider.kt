package org.gdsc.presentation.view.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.presentation.databinding.ItemImageSlideBinding

data class ImageSliderItem(
    val imageUrl: String
)

class ImageSlider(
    private val onItemSelected: () -> Unit = {}
) :
    ListAdapter<ImageSliderItem, ImageSlider.ImageSliderViewHolder>(
        diffUtil
    ) {

    inner class ImageSliderViewHolder(private val binding: ItemImageSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageSliderItem) {
            with(binding) {
                Glide.with(root)
                    .load(item.imageUrl)
                    .into(imageView)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ImageSliderItem>() {
            override fun areItemsTheSame(
                oldItem: ImageSliderItem,
                newItem: ImageSliderItem
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: ImageSliderItem,
                newItem: ImageSliderItem
            ) = oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImageSliderViewHolder(
            ItemImageSlideBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemSelected()
        }
    }

}