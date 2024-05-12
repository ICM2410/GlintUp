package util

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun bitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
    // Create a file in the public external storage.
    val file = File(context.externalCacheDir, "$fileName.png")

    // Convert bitmap to byte array
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream) // PNG is a lossless format, the compression factor (100) is ignored
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fileOutputStream?.flush()
            fileOutputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return file
}