package org.gdsc.presentation.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.domain.model.response.Group
import org.gdsc.presentation.base.BaseViewHolder
import org.gdsc.presentation.base.ViewHolderBindListener
import org.gdsc.presentation.databinding.ItemGroupSelectBinding

class GroupSelectAdapter(
    private val listener: ViewHolderBindListener
): RecyclerView.Adapter<GroupSelectAdapter.GroupSelectViewHolder>() {

    private val groupList = mutableListOf<Group>()

    override fun onBindViewHolder(holder: GroupSelectViewHolder, position: Int) {
        holder.bind(groupList[position])
    }

    class GroupSelectViewHolder(
        binding: ItemGroupSelectBinding,
        listener: ViewHolderBindListener
    ): BaseViewHolder<ItemGroupSelectBinding>(binding, listener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupSelectViewHolder {
        return GroupSelectViewHolder(
            ItemGroupSelectBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), listener
        )
    }

    override fun getItemCount(): Int = groupList.size

    fun submitList(list: List<Group>) {
        groupList.clear()
        groupList.addAll(list)
        notifyDataSetChanged()
    }
}