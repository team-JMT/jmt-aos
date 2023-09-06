package org.gdsc.presentation.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object BitmapUtils {

    fun Bitmap.saveBitmapToFile(context: Context, fileName: String): File {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        directory?.mkdirs()
        val file = File(directory, fileName)

        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            this.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            this.recycle()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }

        return file
    }

    private fun Bitmap.rotateBitmap(angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            this, 0, 0, this.width, this.height, matrix, true
        )
    }

    private fun getOrientationOfImage(inputStream: InputStream): Int {
        val exif = try {
            ExifInterface(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    fun Uri.getCompressedBitmapFromUri(context: Context): Bitmap? {
        val resolver: ContentResolver = context.contentResolver
        var inputStream: InputStream? = null
        try {

            val options = BitmapFactory.Options().apply {
                inSampleSize = 2
            }

            inputStream = resolver.openInputStream(this)

            val bitmap = requireNotNull(BitmapFactory.decodeStream(inputStream, null, options))

            val orientation = getOrientationOfImage(inputStream!!).toFloat()

            return bitmap.rotateBitmap(orientation)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }

}





