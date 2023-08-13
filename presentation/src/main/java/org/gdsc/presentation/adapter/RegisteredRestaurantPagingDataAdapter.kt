package org.gdsc.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.base.Const
import org.gdsc.presentation.databinding.ItemRegisteredRestaurantBinding
import org.gdsc.presentation.utils.CalculatorUtils

class RegisteredRestaurantPagingDataAdapter : PagingDataAdapter<RegisteredRestaurant, RestaurantViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<RegisteredRestaurant>() {
            override fun areItemsTheSame(oldItem: RegisteredRestaurant, newItem: RegisteredRestaurant) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: RegisteredRestaurant, newItem: RegisteredRestaurant) =
                oldItem == newItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {

        return RestaurantViewHolder(
            ItemRegisteredRestaurantBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}

class RestaurantViewHolder(private val binding: ItemRegisteredRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RegisteredRestaurant) {
        with(binding) {
            item.let {
                restaurantName.text = it.name
                restaurantCategory.text = it.category
                userName.text = it.userNickName

                restaurantDistance.text = binding.root.context.getString(
                    R.string.distance_from_current_location,
                    CalculatorUtils.getDistanceWithLength(it.differenceInDistance.toInt())
                )

                Glide.with(root)
                    .load(it.userProfileImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(userProfileImage)

                Glide.with(root)
                    .load(it.restaurantImageUrl)
                    .placeholder(R.drawable.ig_restaurant_placeholder)
                    .into(restaurantImage)
            }
        }
    }
}