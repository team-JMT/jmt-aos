package org.gdsc.presentation.adapter

import org.gdsc.domain.model.MediaItem


interface GalleryImageClickListener {
    fun onImageClick(vararg mediaItem: MediaItem)
}