package org.gdsc.presentation.view.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.RegisteredRestaurant
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.Const
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemRegisteredRestaurantBinding
import org.gdsc.presentation.utils.CalculatorUtils

class RegisteredRestaurantPagingDataAdapter(
    private val listener: ViewHolderBindListener
) : PagingDataAdapter<RegisteredRestaurant, RegisteredRestaurantPagingDataAdapter.RestaurantViewHolder>(
    diffCallback
) {

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
            ), listener
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class RestaurantViewHolder(
        binding: ItemRegisteredRestaurantBinding,
        listener: ViewHolderBindListener
    ) :BaseViewHolder<ItemRegisteredRestaurantBinding>(binding, listener)
}
