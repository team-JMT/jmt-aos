package org.gdsc.presentation.base

import androidx.viewbinding.ViewBinding

interface ViewHolderBindListener {
    open fun onViewHolderBind(holder: BaseViewHolder<out ViewBinding>, _item: Any)
}