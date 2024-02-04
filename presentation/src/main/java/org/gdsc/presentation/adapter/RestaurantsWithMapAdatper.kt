package org.gdsc.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.domain.model.response.RestaurantRegistrationResponse
import org.gdsc.presentation.databinding.ItemRestaurantWithMapBinding

class RestaurantsWithMapAdatper
    : PagingDataAdapter<RegisteredRestaurant, RestaurantsWithMapAdatper.RestaurantsWithMapViewHolder>(diffCallback) {
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
        private val binding: ItemRestaurantWithMapBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RegisteredRestaurant) {
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
        val binding = ItemRestaurantWithMapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantsWithMapViewHolder(binding)
    }
}