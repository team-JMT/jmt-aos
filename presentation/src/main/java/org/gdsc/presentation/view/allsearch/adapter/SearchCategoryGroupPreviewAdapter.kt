package org.gdsc.presentation.view.allsearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.GroupPreview
import org.gdsc.presentation.R
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemSearchGroupBinding

class SearchCategoryGroupPreviewAdapter(
    private val listener: ViewHolderBindListener
)
    : ListAdapter<GroupPreview, SearchCategoryGroupPreviewAdapter.GroupWithSearchPreviewViewHolder>(
    diffCallback
) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<GroupPreview>() {
            override fun areItemsTheSame(
                oldItem: GroupPreview,
                newItem: GroupPreview
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: GroupPreview,
                newItem: GroupPreview
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class GroupWithSearchPreviewViewHolder(
        binding: ItemSearchGroupBinding,
        listener: ViewHolderBindListener
    ): BaseViewHolder<ItemSearchGroupBinding>(binding, listener)

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
        return GroupWithSearchPreviewViewHolder(binding, listener)
    }

}