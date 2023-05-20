package com.r42914lg.chatsandbox.slider

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPager(

) {
    HorizontalPager(pageCount = 10) { page ->
        // Our page content
        Text(
            text = "Page: $page",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}