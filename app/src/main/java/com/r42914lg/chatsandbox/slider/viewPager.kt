package com.r42914lg.chatsandbox.slider

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPager() {
    HorizontalPager(
        pageCount = 5,
    ) { page ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Page Index : $page")
        }
    }
}