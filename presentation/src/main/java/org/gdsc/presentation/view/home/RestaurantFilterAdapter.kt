package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemMapWithRestaurantFilterBinding

class RestaurantFilterAdapter(
    private val listener: ViewHolderBindListener
): RecyclerView.Adapter<RestaurantFilterAdapter.RestaurantFilterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantFilterViewHolder {
        return RestaurantFilterViewHolder(
            ItemMapWithRestaurantFilterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: RestaurantFilterViewHolder, position: Int) {
        holder.bind(Unit)
    }

    override fun getItemCount(): Int = 1

    class RestaurantFilterViewHolder(
        binding: ItemMapWithRestaurantFilterBinding,
        listener: ViewHolderBindListener
    ): BaseViewHolder<ItemMapWithRestaurantFilterBinding>(binding, listener)
}