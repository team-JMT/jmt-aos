package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemMapWithRestaurantBinding

class MapMarkerWithRestaurantsAdatper(
    private val listener: ViewHolderBindListener
)
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
                return oldItem.id == newItem.id
            }
        }
    }

    class RestaurantsWithMapViewHolder(
        binding: ItemMapWithRestaurantBinding,
        listener: ViewHolderBindListener
    ): BaseViewHolder<ItemMapWithRestaurantBinding>(binding, listener)

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
        val binding = ItemMapWithRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false,)
        return RestaurantsWithMapViewHolder(binding, listener)
    }
}