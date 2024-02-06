package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ItemRecommendPopularRestaurantBinding
import org.gdsc.presentation.view.custom.FlexibleCornerImageView

class RecommendPopularRestaurantAdapter(
    private val dataList: List<RegisteredRestaurant>
): RecyclerView.Adapter<RecommendPopularRestaurantAdapter.RecommendPopularRestaurantViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : RecommendPopularRestaurantViewHolder {
        val binding = ItemRecommendPopularRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return RecommendPopularRestaurantViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecommendPopularRestaurantViewHolder,
        position: Int
    ) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class RecommendPopularRestaurantViewHolder(
        private val binding: ItemRecommendPopularRestaurantBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RegisteredRestaurant) {
            Glide.with(itemView.context)
                .load(item.restaurantImageUrl)
                .placeholder(R.drawable.base_profile_image)
                .into(binding.restaurantImage)

            binding.userName.text = item.userNickName

            Glide.with(itemView.context)
                .load(item.userProfileImageUrl)
                .placeholder(R.drawable.base_profile_image)
                .into(binding.userProfileImage)

            binding.restaurantName.text = item.name
        }
    }
}