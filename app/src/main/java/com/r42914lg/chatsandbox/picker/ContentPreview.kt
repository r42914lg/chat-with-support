package com.r42914lg.chatsandbox.picker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ContentPreview(
    uriList: List<Uri> = globalUriList,
) {
    val context = LocalContext.current
    val cR = context.contentResolver
    val mime: MimeTypeMap = MimeTypeMap.getSingleton()

    // 0 - other, 1 - image, 2 - video
    fun checkMimeType(uri: Uri): Int {
        var retVal = 0

        if (mime.getExtensionFromMimeType(cR.getType(uri)) == "jpg")
            retVal = 1
        else if (mime.getExtensionFromMimeType(cR.getType(uri)) == "mp4")
            retVal = 2

        return retVal
    }

    Column {
        uriList.forEach {
            when (checkMimeType(it)) {
                1 -> ImageThumbnail(it)
                2 -> VideoThumbnail(it)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageThumbnail(
    uri: Uri,
) {
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val source = ImageDecoder.createSource(context.contentResolver, uri)
    bitmap.value = ImageDecoder.decodeBitmap(source)

    bitmap.value?.let { btm ->
        Image(
            bitmap = btm.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
        )
    }
}

@Composable
fun VideoThumbnail(
    uri: Uri,
) {
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    fun createVideoThumb(context: Context, uri: Uri): Bitmap? {
        try {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(context, uri)

            return mediaMetadataRetriever.frameAtTime
        } catch (ex: Exception) {
            // just suppress
        }
        return null
    }

    bitmap.value = createVideoThumb(context, uri)
    bitmap.value?.let { btm ->
        Image(
            bitmap = btm.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
        )
    }
}
