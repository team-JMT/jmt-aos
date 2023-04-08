package org.gdsc.presentation.view.restaurantregistration

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.domain.model.RestaurantLocationInfo
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.RestaurantLocationInfoItemBinding

class RestaurantLocationInfoAdapter(private val onItemClicked: (RestaurantLocationInfo) -> Unit) :
    ListAdapter<RestaurantLocationInfo, RestaurantLocationInfoAdapter.RestaurantInfoViewHolder>(
        diffUtil
    ) {

    private lateinit var targetString: String

    inner class RestaurantInfoViewHolder(private val binding: RestaurantLocationInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantLocationInfo) {
            setTextSpan(item.placeName)
            binding.restaurantAddress.text = item.addressName
            binding.distanceFromCurrentLocation.text = item.distance
        }

        private fun setTextSpan(placeName: String) {

            val spannable = SpannableString(placeName)
            val startIndex = placeName.indexOf(targetString)

            if (startIndex == NO_MATCHING) {
                binding.restaurantName.text = placeName
            } else {
                val endIndex = startIndex + targetString.length
                spannable.setSpan(
                    ForegroundColorSpan(binding.root.context.getColor(R.color.main600)),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.restaurantName.text = spannable
            }
        }
    }

    fun setTargetString(targetString: String) {
        this.targetString = targetString
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RestaurantInfoViewHolder(RestaurantLocationInfoItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: RestaurantInfoViewHolder, position: Int) {
        val item = getItem(position)

        holder.apply {
            bind(item)
            itemView.setOnClickListener {
                onItemClicked(item)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RestaurantLocationInfo>() {
            override fun areItemsTheSame(
                oldItem: RestaurantLocationInfo,
                newItem: RestaurantLocationInfo
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: RestaurantLocationInfo,
                newItem: RestaurantLocationInfo
            ) = oldItem == newItem

        }

        private const val NO_MATCHING = -1
    }
}