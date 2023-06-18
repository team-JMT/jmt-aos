package org.gdsc.presentation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import org.gdsc.domain.model.MediaItem
import org.gdsc.presentation.databinding.ItemImageCheckboxBinding
import org.gdsc.presentation.viewmodel.ImagePickerViewModel


class SingleImagePickerAdapter(
    private val parentViewModel: ImagePickerViewModel
): PagingDataAdapter<MediaItem, SingleImagePickerAdapter.ImageViewHolder>(ImageDiffCallback) {
    private lateinit var listener: GalleryImageClickListener
    fun setListener(listener: GalleryImageClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ImageViewHolder(binding)
        subscribeUi(binding, holder)
        return holder
    }

    private fun subscribeUi(binding: ItemImageCheckboxBinding, holder: ImageViewHolder) {
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
        private val binding: ItemImageCheckboxBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: MediaItem) {

            binding.checkBox.isVisible = false

            val requestBuilder: RequestBuilder<Drawable> =
                Glide.with(binding.root.context)
                    .asDrawable().sizeMultiplier(0.1f)

            Glide.with(binding.root)
                .load(mediaItem.uri.toUri())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .thumbnail(requestBuilder)
                .into(binding.image)
        }
    }
    companion object ImageDiffCallback: DiffUtil.ItemCallback<MediaItem>() {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem == newItem
        }
    }
}
