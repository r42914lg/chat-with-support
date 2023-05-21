package com.r42914lg.chatsandbox.picker

import android.graphics.Bitmap
import android.graphics.ImageDecoder
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
    fun mimeTypeIsImage(uri: Uri) =
        mime.getExtensionFromMimeType(cR.getType(uri))?.contains("image") ?: true

    Column {
        uriList.forEach {
            //if (mimeTypeIsImage(it))
                ImageThumbnail(it)
            //else
            //    VideoThumbnail(it)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImageThumbnail(
    uri: Uri
) {
    val bitmap =  remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val source = ImageDecoder.createSource(context.contentResolver, uri)
    bitmap.value = ImageDecoder.decodeBitmap(source)

    bitmap.value?.let {  btm ->
        Image(bitmap = btm.asImageBitmap(),
            contentDescription =null,
            modifier = Modifier.size(50.dp))
    }
}

