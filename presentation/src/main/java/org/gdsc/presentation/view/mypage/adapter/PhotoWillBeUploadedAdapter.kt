package org.gdsc.presentation.view.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.presentation.databinding.ItemPhotoWillBeUploadedBinding

class PhotoWillBeUploadedAdapter(
    private val onDeleteButtonClicked: (String) -> Unit
) :
    ListAdapter<String, PhotoWillBeUploadedAdapter.PhotoWillBeUploadedViewHolder>(
        diffUtil
    ) {
    inner class PhotoWillBeUploadedViewHolder(private val binding: ItemPhotoWillBeUploadedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {

            Glide.with(binding.root)
                .load(url.toUri())
                .into(binding.photoWillBeUploaded)

            binding.deleteButton.setOnClickListener {
                onDeleteButtonClicked(url)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoWillBeUploadedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PhotoWillBeUploadedViewHolder(
            ItemPhotoWillBeUploadedBinding.inflate(
                inflater,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PhotoWillBeUploadedViewHolder, position: Int) {
        holder.apply {
            bind(getItem(position))
        }
    }
}