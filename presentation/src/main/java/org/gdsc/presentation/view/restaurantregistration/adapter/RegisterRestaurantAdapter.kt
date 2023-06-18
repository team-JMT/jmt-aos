package org.gdsc.presentation.view.restaurantregistration.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import org.gdsc.presentation.databinding.ItemSelectedRestaurantBinding

class RegisterRestaurantAdapter: RecyclerView.Adapter<RegisterRestaurantAdapter.RestaurantViewHolder>() {

    private val restaurantList = mutableListOf<String>()

    inner class RestaurantViewHolder(private val binding: ItemSelectedRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(url:String) {

                val requestBuilder: RequestBuilder<Drawable> =
                    Glide.with(binding.root.context)
                        .asDrawable().sizeMultiplier(0.3f)

                Glide.with(binding.root)
                    .load(url.toUri())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .thumbnail(requestBuilder)
                    .into(binding.restaurantImage)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RestaurantViewHolder(
            ItemSelectedRestaurantBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.apply {
            bind(restaurantList[position])
        }
    }

    override fun getItemCount(): Int = restaurantList.size

    fun submitList(list: Array<String>) {
        restaurantList.clear()
        restaurantList.addAll(list)
        notifyDataSetChanged()
    }
}