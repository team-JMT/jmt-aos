package org.gdsc.presentation.adapter

import org.gdsc.domain.model.ImageItem


interface GalleryImageClickListener {
    fun onImageClick(imageItem: ImageItem)
}