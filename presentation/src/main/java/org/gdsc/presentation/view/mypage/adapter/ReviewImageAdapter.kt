package org.gdsc.presentation.view.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.presentation.databinding.ItemReviewImageBinding

class ReviewImageAdapter()  :
    ListAdapter<String, ReviewImageAdapter.ReviewImageViewHolder>(
        diffUtil
    ) {
    inner class ReviewImageViewHolder(private val binding: ItemReviewImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {

            Glide.with(binding.root)
                .load(url)
                .into(binding.image)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReviewImageViewHolder(
            ItemReviewImageBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewImageViewHolder, position: Int) {
        holder.apply {
            bind(getItem(position))
        }
    }
}