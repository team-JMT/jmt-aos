package org.gdsc.presentation.view.allsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.GroupInfo
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemSearchGroupBinding

class SearchCategoryGroupAdapter(
    private val listener: ViewHolderBindListener,
) :
    PagingDataAdapter<GroupInfo, SearchCategoryGroupAdapter.SearchCategoryGroupViewHolder>(
        DiffCallback
    ) {

    companion object DiffCallback : DiffUtil.ItemCallback<GroupInfo>() {
        override fun areItemsTheSame(oldItem: GroupInfo, newItem: GroupInfo): Boolean {
            return oldItem.groupId == newItem.groupId
        }

        override fun areContentsTheSame(oldItem: GroupInfo, newItem: GroupInfo): Boolean {
            return oldItem == newItem
        }
    }

    class SearchCategoryGroupViewHolder(
        binding: ItemSearchGroupBinding,
        listener: ViewHolderBindListener,
    ) : BaseViewHolder<ItemSearchGroupBinding>(
        binding, listener
    )

    override fun onBindViewHolder(
        holder: SearchCategoryGroupViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchCategoryGroupViewHolder {
        val binding =
            ItemSearchGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchCategoryGroupViewHolder(binding, listener)
    }
}