package org.gdsc.presentation.view.allsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ItemSearchRestaurantBinding

class SearchCategoryRestaurantAdapter
    : PagingDataAdapter<RegisteredRestaurant, SearchCategoryRestaurantAdapter.RestaurantsWithSearchViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RegisteredRestaurant>() {
            override fun areItemsTheSame(
                oldItem: RegisteredRestaurant,
                newItem: RegisteredRestaurant
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RegisteredRestaurant,
                newItem: RegisteredRestaurant
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class RestaurantsWithSearchViewHolder(
        private val binding: ItemSearchRestaurantBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RegisteredRestaurant) {
            binding.run {
                Glide.with(itemView.context)
                    .load(item.userProfileImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(userProfileImage)

                userName.text = item.userNickName

                Glide.with(itemView.context)
                    .load(item.restaurantImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(restaurantImage)

                restaurantCategory.text = item.category
                restaurantName.text = item.name
            }
        }
    }

    override fun onBindViewHolder(holder: RestaurantsWithSearchViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantsWithSearchViewHolder {
        val binding = ItemSearchRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCategoryRestaurantAdapter.RestaurantsWithSearchViewHolder(binding)
    }
}