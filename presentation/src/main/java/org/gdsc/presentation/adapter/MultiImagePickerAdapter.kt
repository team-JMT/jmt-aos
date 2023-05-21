package org.gdsc.presentation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import org.gdsc.domain.model.MediaItem
import org.gdsc.presentation.databinding.ItemImageBinding
import org.gdsc.presentation.databinding.ItemImageCheckboxBinding
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

class MultiImagePickerAdapter(
    private val parentViewModel: ImagePickerViewModel
): PagingDataAdapter<MediaItem, MultiImagePickerAdapter.ImageViewHolder>(ImageDiffCallback) {
    private lateinit var listener: GalleryImageClickListener
    private val checkBoxes = mutableListOf<CheckBox>()

    private fun checkBoxSize(): Int = checkBoxes.size
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
            binding.checkbox.isChecked = binding.checkbox.isChecked.not()
            if(binding.checkbox.isChecked) {
                binding.checkbox.text = checkBoxSize().inc().toString()
                checkBoxes.add(binding.checkbox)
            } else {
                checkBoxes.indexOf(binding.checkbox).let { index ->
                    checkBoxes.subList(index, checkBoxSize()).forEachIndexed { i, checkBox ->
                        checkBox.text = (index + i).toString()
                    }
                }
                binding.checkbox.text = ""
                checkBoxes.remove(binding.checkbox)
            }
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
            binding.checkbox.isVisible = true

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