package org.gdsc.presentation.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<B: ViewBinding>(
    private val binding: B,
    private val listener: ViewHolderBindListener? = null,
) : RecyclerView.ViewHolder(binding.root) {

    open fun bind(_item: Any) {
        listener?.onViewHolderBind(this, _item)
    }
}