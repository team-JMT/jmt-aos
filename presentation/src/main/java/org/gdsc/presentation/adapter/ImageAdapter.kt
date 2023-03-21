package com.example.customimagepicker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customimagepicker.data.ImageItem
import org.gdsc.presentation.databinding.ItemImageBinding
import org.gdsc.presentation.viewmodel.ImagePickerViewModel

/**
 * @param parentViewModel The ImagePicker's ViewModel which holds each ImageItem
 * whose isChecked should be updated when checkbox checked.
 */
class ImageAdapter(
    private val parentViewModel: ImagePickerViewModel
): ListAdapter<ImageItem, RecyclerView.ViewHolder>(ImageDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val holder = ImageViewHolder(binding)
        subscribeUi(binding, holder)

        return holder
    }

    private fun subscribeUi(binding: ItemImageBinding, holder: ImageViewHolder) {
        binding.image.setOnClickListener { view ->
            binding.checkbox.isChecked = !binding.checkbox.isChecked
        }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            parentViewModel.imageItemList.value?.let {
                val position = holder.absoluteAdapterPosition
                it[position].isChecked = isChecked
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageItem = getItem(position)
        (holder as ImageViewHolder).bind(imageItem)
    }

    class ImageViewHolder(
        private val binding: ItemImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageItem: ImageItem) {
            Log.d("PickerTest","uri : ${imageItem.uri}")
            Glide.with(binding.root)
                .load(imageItem.uri)
                .into(binding.image)
        }

    }
}
private class ImageDiffCallback: DiffUtil.ItemCallback<ImageItem>() {
    override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
        return oldItem == newItem
    }
}