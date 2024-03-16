package org.gdsc.presentation.view.allsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemSearchRestaurantBinding

class SearchCategoryRestaurantAdapter(
    private val listener: ViewHolderBindListener
) : PagingDataAdapter<RegisteredRestaurant, SearchCategoryRestaurantAdapter.RestaurantsWithSearchViewHolder>(
    diffCallback
) {

        private var maxItemCount = Int.MAX_VALUE
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
        binding: ItemSearchRestaurantBinding,
        listener: ViewHolderBindListener
    ): BaseViewHolder<ItemSearchRestaurantBinding>(binding, listener)

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
        return RestaurantsWithSearchViewHolder(binding, listener)
    }

    fun setMaxItemCount(@IntRange(from = 1, to = Long.MAX_VALUE) maxItemCount: Int) {
        this.maxItemCount = maxItemCount
    }
}