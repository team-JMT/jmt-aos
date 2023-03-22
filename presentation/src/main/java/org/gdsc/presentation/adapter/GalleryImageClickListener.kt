package org.gdsc.presentation.adapter

import org.gdsc.presentation.data.ImageItem


interface GalleryImageClickListener {
    fun onImageClick(imageItem: ImageItem)
}