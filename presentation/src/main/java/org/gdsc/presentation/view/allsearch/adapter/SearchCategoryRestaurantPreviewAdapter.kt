package org.gdsc.presentation.view.allsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemSearchRestaurantBinding

class SearchCategoryRestaurantPreviewAdapter(
    private val listener: ViewHolderBindListener
)
    : ListAdapter<RegisteredRestaurant, SearchCategoryRestaurantPreviewAdapter.RestaurantsWithSearchPreviewViewHolder>(
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

    class RestaurantsWithSearchPreviewViewHolder(
        binding: ItemSearchRestaurantBinding,
        listener: ViewHolderBindListener
    ): BaseViewHolder<ItemSearchRestaurantBinding>(
        binding, listener
    )

    override fun onBindViewHolder(holder: RestaurantsWithSearchPreviewViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantsWithSearchPreviewViewHolder {
        val binding = ItemSearchRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantsWithSearchPreviewViewHolder(binding, listener)
    }

}