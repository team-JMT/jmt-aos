package org.gdsc.presentation.view.allsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.GroupInfo
import org.gdsc.presentation.R
import org.gdsc.presentation.databinding.ItemSearchGroupBinding

class SearchCategoryGroupPreviewAdapter
    : ListAdapter<GroupInfo, SearchCategoryGroupPreviewAdapter.GroupWithSearchPreviewViewHolder>(
    diffCallback
) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<GroupInfo>() {
            override fun areItemsTheSame(
                oldItem: GroupInfo,
                newItem: GroupInfo
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: GroupInfo,
                newItem: GroupInfo
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class GroupWithSearchPreviewViewHolder(
        private val binding: ItemSearchGroupBinding,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GroupInfo) {
            binding.run {
                Glide.with(itemView.context)
                    .load(item.groupProfileImageUrl)
                    .placeholder(R.drawable.base_profile_image)
                    .into(ivGroupImage)

                tvGroupName.text = item.groupName
                tvIntroduction.text = item.groupIntroduce
                tvMemberCount.text = item.memberCnt.toString()
                tvRestaurantCount.text = item.restaurantCnt.toString()
            }
        }
    }

    override fun onBindViewHolder(holder: GroupWithSearchPreviewViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupWithSearchPreviewViewHolder {
        val binding = ItemSearchGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupWithSearchPreviewViewHolder(binding)
    }

}