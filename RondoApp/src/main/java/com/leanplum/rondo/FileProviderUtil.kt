package com.leanplum.rondo

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

object FileProviderUtil {
    val mimeTypes = listOf("image/*", "application/pdf", "text/*", "video/mp4", "audio/mpeg")
}

fun Context.openFileFromProviderWithChooser(filePath: String, mimeType: String? = null): Boolean {
    val intent = Intent(Intent.ACTION_VIEW)

    val file = File(filePath)
    if (!file.exists()) {
        Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show()
        return false
    }
    val uri = try {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
    } catch (iae: IllegalArgumentException) {
        Toast.makeText(this, "Unsupported file path", Toast.LENGTH_SHORT).show()
        return false
    }

    intent.setDataAndType(uri, mimeType)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    val chooserIntent = Intent.createChooser(intent, "")
    return try {
        startActivity(chooserIntent)
        true
    } catch (anfe: ActivityNotFoundException) {
        Toast.makeText(this, "No activity found to open the file", Toast.LENGTH_SHORT)
            .show()
        false
    }
}

fun Context.openFileFromProviderWithChooser(filePath: String) {
    val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
    if (extension != null) {
        val type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        if (type != null) {
            openFileFromProviderWithChooser(filePath, type)
            return
        }
    }

    Toast.makeText(this, "MIME type for file not found: $filePath", Toast.LENGTH_SHORT).show()
}
