package org.gdsc.presentation.view.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.Review
import org.gdsc.presentation.databinding.ItemReviewRestaurantBinding

class RestaurantReviewAdapter(
    private val onItemSelected: () -> Unit
) :
    ListAdapter<Review, RestaurantReviewAdapter.RestaurantReviewViewHolder>(
        diffUtil
    ) {

    inner class RestaurantReviewViewHolder(private val binding: ItemReviewRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            with(binding) {
                Glide.with(root)
                    .load(item.reviewerImageUrl)
                    .into(ivProfile)

                tvNickname.text = item.userName
                tvContent.text = item.reviewContent
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(
                oldItem: Review,
                newItem: Review
            ) = oldItem.reviewId == newItem.reviewId

            override fun areContentsTheSame(
                oldItem: Review,
                newItem: Review
            ) = oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RestaurantReviewViewHolder(
            ItemReviewRestaurantBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RestaurantReviewViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemSelected()
        }
    }

}