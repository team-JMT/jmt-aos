package org.gdsc.domain.model

data class Image(
    val id: Long,
    val uri: String,
    val bucket: String,
    val dateAdded: Long,
    val fileSize: Long,
    val mimeType: String,
    val albumName: String,
    val orientation: Int
) {
    override fun toString(): String {
        return "id = $id, bucketId = $bucket, uri = $uri dateAdded = $dateAdded, fileSize = $fileSize, mimeType = $mimeType, albumName = $albumName, orientation = $orientation"
    }
}

