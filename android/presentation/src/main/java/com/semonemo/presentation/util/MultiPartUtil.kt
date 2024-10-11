package com.semonemo.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

fun String.toRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())

fun Uri.toAbsolutePath(context: Context): String {
    val contentResolver = context.contentResolver
    var fileName: String? = null

    contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        fileName = cursor.getString(nameIndex)
    }

    val file =
        fileName?.let {
            val media = File(context.filesDir, "media")
            media.mkdirs()
            File(media, it)
        }

    contentResolver.openInputStream(this).use { input ->
        FileOutputStream(file).use { output ->
            input?.copyTo(output)
        }
    }
    return file?.absolutePath ?: ""
}
