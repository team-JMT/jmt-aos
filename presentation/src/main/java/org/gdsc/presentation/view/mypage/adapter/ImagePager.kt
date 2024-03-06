package org.gdsc.presentation.view.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.presentation.databinding.ItemImagePageBinding

data class ImagePagerItem(
    val imageUrl: String
)

class ImagePager(
    private val onItemSelected: () -> Unit
) :
    ListAdapter<ImagePagerItem, ImagePager.ImagePagerViewHolder>(
        diffUtil
    ) {

    inner class ImagePagerViewHolder(private val binding: ItemImagePageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImagePagerItem, position: Int) {
            with(binding) {
                Glide.with(root)
                    .load(item.imageUrl)
                    .into(imageView)

                tvSequence.text = "$position / $itemCount"
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ImagePagerItem>() {
            override fun areItemsTheSame(
                oldItem: ImagePagerItem,
                newItem: ImagePagerItem
            ) = oldItem === newItem

            override fun areContentsTheSame(
                oldItem: ImagePagerItem,
                newItem: ImagePagerItem
            ) = oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePagerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ImagePagerViewHolder(
            ItemImagePageBinding.inflate(
                inflater,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ImagePagerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position + 1)
        holder.itemView.setOnClickListener {
            onItemSelected()
        }
    }

}