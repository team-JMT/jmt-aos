package org.gdsc.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.gdsc.domain.model.MediaItem
import org.gdsc.presentation.databinding.ItemImageBinding
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

/**
 * @param parentViewModel The ImagePicker's ViewModel which holds each ImageItem
 * whose isChecked should be updated when checkbox checked.
 */
class ImageAdapter(
    private val parentViewModel: ImagePickerViewModel
): PagingDataAdapter<MediaItem, ImageAdapter.ImageViewHolder>(ImageDiffCallback) {
    private lateinit var listener: GalleryImageClickListener
    fun setListener(listener: GalleryImageClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ImageViewHolder(binding)
        subscribeUi(binding, holder)
        return holder
    }

    private fun subscribeUi(binding: ItemImageBinding, holder: ImageViewHolder) {
        binding.image.setOnClickListener {
            getItem(holder.absoluteAdapterPosition)?.let { listener.onImageClick(it) }
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageItem = getItem(position)
        if (imageItem != null) {
            holder.bind(imageItem)
        }

    }

    class ImageViewHolder(
        private val binding: ItemImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: MediaItem) {
            Glide.with(binding.root)
                .load(mediaItem.uri.toUri())
                .into(binding.image)
        }
    }
    companion object ImageDiffCallback: DiffUtil.ItemCallback<MediaItem>() {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem == newItem
        }
    }
}
