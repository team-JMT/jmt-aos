package org.gdsc.presentation.view.restaurantregistration.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import org.gdsc.domain.model.MediaItem
import org.gdsc.presentation.adapter.GalleryImageClickListener
import org.gdsc.presentation.databinding.ItemImageCheckboxBinding

class MultiImagePickerAdapter(): PagingDataAdapter<MediaItem, MultiImagePickerAdapter.ImageViewHolder>(ImageDiffCallback) {
    private lateinit var listener: GalleryImageClickListener
    private val checkedList = mutableListOf<Long>()

    // 갤러리 선택에 맞게 이미지 배열을 선택 할 수 있도록, 전체 이미지에 대한 id 값을 갖는 배열을 포함
    // 갤러리 선택에 맞게 각 파트로 쪼개지는 배열을 포함

    private fun checkedListSize(): Int = checkedList.size
    fun setListener(listener: GalleryImageClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ImageViewHolder(binding)

        return holder
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageItem = getItem(position)
        if (imageItem != null) {
            holder.bind(position, imageItem)
        }
    }

    inner class ImageViewHolder(
        private val binding: ItemImageCheckboxBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(position:Int, mediaItem: MediaItem) {

            binding.image.setOnClickListener {
                if(checkedListSize() == 10 && binding.checkBox.isChecked.not()) {
                    Toast.makeText(it.context, "이미지는 10개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                binding.checkBox.isChecked = binding.checkBox.isChecked.not()

                if(binding.checkBox.isChecked) {
                    binding.checkBox.text = checkedListSize().inc().toString()
                    checkedList.add(mediaItem.id)
                    binding.imageViewBorder.isVisible = true
                } else {
                    val itemPosition = checkedList.indexOf(mediaItem.id)
                    checkedList.removeAt(itemPosition)
                    notifyDataSetChanged()
                }

                listener.onImageClick(mediaItem)
            }

            binding.checkBox.isVisible = true
            if(mediaItem.id in checkedList) {
                binding.apply {
                    imageViewBorder.isVisible = true
                    checkBox.isChecked = true
                    checkBox.text = checkedList.indexOf(mediaItem.id).inc().toString()
                }
            } else {
                binding.apply {
                    imageViewBorder.isVisible = false
                    checkBox.isChecked = false
                    checkBox.text = ""
                }

            }

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