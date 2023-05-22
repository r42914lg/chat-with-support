package com.r42914lg.chatsandbox.slider

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes

@Composable
fun VideoPlayer(
    uri: Uri
) {
    val mContext = LocalContext.current

    val mediaItem = MediaItem.Builder()
        .setUri(uri)
        .setMimeType(MimeTypes.APPLICATION_MP4)
        .build()

    val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSource.Factory(mContext))
        .createMediaSource(mediaItem)

    val mExoPlayer = remember(mContext) {
        ExoPlayer.Builder(mContext).build().apply {
            setMediaSource(mediaSource)
            playWhenReady = true
            seekTo(0, 0L)
            prepare()
        }
    }

    AndroidView(
        factory = {
                context -> StyledPlayerView(context).apply {
                    player = mExoPlayer
                }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VideoPlayer(Uri.parse("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"))
}
