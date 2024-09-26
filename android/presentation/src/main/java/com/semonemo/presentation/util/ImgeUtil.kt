package com.semonemo.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64

fun encodeImageToBase64FromUri(
    context: Context,
    uri: Uri?,
): String? {
    if (uri == null) {
        return null
    }
    val filePath = uri.toAbsolutePath(context)
    val file = File(filePath)

    return try {
        val imageBytes = file.readBytes()
        Base64.getEncoder().encodeToString(imageBytes)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun decodeBase64ToImage(base64String: String): Uri? {
    val imageBytes = Base64.getDecoder().decode(base64String)
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Uri.parse(
        "data:image/png;base64," + Base64.getEncoder().encodeToString(byteArray),
    )
}
