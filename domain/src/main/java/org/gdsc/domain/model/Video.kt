package org.gdsc.domain.model

data class Video(
                 val id: Long,
                 val uri: String,
                 val bucket: String,
                 val dateAdded: Long,
                 val fileSize: Long,
                 val mimeType: String,
                 val duration: Long,
                 val albumName: String,
                 val orientation: Int
) {
    override fun toString(): String {
        return "id = $id, bucketId = $bucket, uri = $uri dateAdded = $dateAdded, fileSize = $fileSize, duration = $duration, mimeType = $mimeType, albumName = $albumName, orientation = $orientation"
    }
}
