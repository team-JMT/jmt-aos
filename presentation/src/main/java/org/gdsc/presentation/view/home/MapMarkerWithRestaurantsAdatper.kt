package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ItemMapWithRestaurantBinding

class MapMarkerWithRestaurantsAdatper
    : PagingDataAdapter<RegisteredRestaurant, MapMarkerWithRestaurantsAdatper.RestaurantsWithMapViewHolder>(
    diffCallback
) {
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

    inner class RestaurantsWithMapViewHolder(
        private val binding: ItemMapWithRestaurantBinding,
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
                drinkAvailability.text = if (item.canDrinkLiquor) "주류 가능" else "주류 불가능"

                restaurantName.text = item.name
                restaurantDesc.text = item.introduce
            }
        }
    }
    override fun onBindViewHolder(holder: RestaurantsWithMapViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantsWithMapViewHolder {
        val binding = ItemMapWithRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantsWithMapViewHolder(binding)
    }
}