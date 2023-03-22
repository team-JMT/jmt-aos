package org.gdsc.domain.repository

interface GalleryRepository {
    fun getGalleryImage(): ArrayList<String>
}