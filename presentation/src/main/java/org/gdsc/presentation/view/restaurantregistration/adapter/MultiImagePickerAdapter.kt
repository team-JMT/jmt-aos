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
import org.gdsc.domain.Empty
import org.gdsc.domain.model.MediaItem
import org.gdsc.presentation.adapter.GalleryImageClickListener
import org.gdsc.presentation.databinding.ItemImageCheckboxBinding
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

class MultiImagePickerAdapter(
    private val parentViewModel: ImagePickerViewModel
): PagingDataAdapter<MediaItem, MultiImagePickerAdapter.ImageViewHolder>(ImageDiffCallback) {
    private lateinit var listener: GalleryImageClickListener
    private val checkBoxList = mutableListOf<CheckBoxData>()
    private val checkedList = mutableListOf<Long>()

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
            if(position >= checkBoxList.size)
                checkBoxList.add(position, CheckBoxData(mediaItem.id, false))

            binding.checkBox.isVisible = true
            binding.imageViewBorder.isVisible = if(checkBoxList[position].id == mediaItem.id) checkBoxList[position].checked else false
            binding.checkBox.isChecked = checkBoxList[position].checked
            binding.checkBox.text = checkBoxList[position].text

            binding.image.setOnClickListener {
                if(checkedListSize() == 10 && binding.checkBox.isChecked.not()) {
                    Toast.makeText(it.context, "이미지는 10개까지 선택이 가능합니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                binding.checkBox.isChecked = binding.checkBox.isChecked.not()

                if(binding.checkBox.isChecked) {
                    binding.checkBox.text = checkedListSize().inc().toString()
                    checkBoxList[position].text = binding.checkBox.text.toString()
                    checkBoxList[position].checked = binding.checkBox.isChecked
                    checkedList.add(checkBoxList[position].id)
                    binding.imageViewBorder.isVisible = true
                } else {
                    for (i in 0 until checkedListSize()) {
                        if (checkedList[i] == checkBoxList[position].id) {
                            for (j in i until checkedListSize()) {
                                checkBoxList.findByID(checkedList[j])?.let { num ->
                                    checkBoxList[num].text = j.toString()
                                }
                            }

                            binding.imageViewBorder.isVisible = false
                            checkBoxList[position].text = String.Empty
                            checkBoxList[position].checked = false
                            checkedList.removeAt(i)
                            break
                        }
                    }
                    notifyDataSetChanged()
                }

                listener.onImageClick(mediaItem)
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


data class CheckBoxData(
    val id: Long,
    var checked: Boolean,
    var text: String = String.Empty
)

fun MutableList<CheckBoxData>.findByID(id: Long): Int? {
    for (i in 0 until this.size) {
        if (this[i].id == id) {
            return i
        }
    }
    return null
}