package com.r42914lg.chatsandbox.conversation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val RIGHT_SIDE_PADDING = 16
const val AVATAR_PLACEHOLDER = 54
const val BUBBLE_LEFT_OR_RIGHT_SIDE_PADDING = 50
const val BUBBLE_INTERNAL_TEXT_SIDE_PADDING_HOR = 12
const val BUBBLE_INTERNAL_TEXT_SIDE_PADDING_VERT = 4
const val TIME_RIGHT_PADDING = 6
const val TIME_TO_TEXT_PADDING = 4
const val EXTRA_LINE_HEIGHT = 20

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTextApi::class)
@Composable
fun checkTimeOverLap(
    content: String,
    isUserMe: Boolean,
    time: String,
): Pair<Int, Int> {
    val configuration = LocalConfiguration.current
    val maxAllowedWidthDp = remember {
        (
                configuration.screenWidthDp -
                        AVATAR_PLACEHOLDER -
                        RIGHT_SIDE_PADDING -
                        BUBBLE_LEFT_OR_RIGHT_SIDE_PADDING -
                        BUBBLE_INTERNAL_TEXT_SIDE_PADDING_HOR * 2
                )
    }
    val textMeasurer = rememberTextMeasurer()

    val textToDraw = messageFormatter(
        text = content,
        primary = isUserMe,
    )
    val maxAllowedWidthPx = with(LocalDensity.current) { maxAllowedWidthDp.toDp().toPx() }
    val textLayoutResult = remember(textToDraw) {
        textMeasurer.measure(
            text = textToDraw,
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = FontFamily.Default,
            ),
            constraints = Constraints(maxWidth = maxAllowedWidthPx.toInt()),
        )
    }

    val timeToDraw = toLocalDateTime(time).asHhMmTime()
    val timeLayoutResult = remember(timeToDraw) {
        textMeasurer.measure(
            timeToDraw,
            TextStyle(
                fontSize = 11.sp,
                fontFamily = FontFamily.Default,
            ),
        )
    }

    var maxTextWidthPx = 0f
    for (i in 0 until textLayoutResult.lineCount)
        if (textLayoutResult.getLineRight(i) > maxTextWidthPx)
            maxTextWidthPx = textLayoutResult.getLineRight(i)

    val lastLineWidthPx = textLayoutResult.getLineRight(textLayoutResult.lineCount - 1)
    val timeWidthPx = timeLayoutResult.getLineRight(0)

    val maxTextWidthDp = with(LocalDensity.current) { maxTextWidthPx.toDp().value }
    val lastLineWidthDp = with(LocalDensity.current) { lastLineWidthPx.toDp().value }
    val timeWidthDp = with(LocalDensity.current) { timeWidthPx.toDp().value + TIME_TO_TEXT_PADDING }
    val heightDp = with(LocalDensity.current) { textLayoutResult.size.height.toDp().value }

    return if (lastLineWidthDp + timeWidthDp - TIME_RIGHT_PADDING > maxAllowedWidthDp)
        Pair(
            maxAllowedWidthDp + BUBBLE_INTERNAL_TEXT_SIDE_PADDING_HOR * 2,
            heightDp.toInt() + BUBBLE_INTERNAL_TEXT_SIDE_PADDING_VERT * 2 + EXTRA_LINE_HEIGHT,
        )
    else {
        if (lastLineWidthDp + timeWidthDp - TIME_RIGHT_PADDING < maxTextWidthDp)
            Pair(
                (maxTextWidthDp + BUBBLE_INTERNAL_TEXT_SIDE_PADDING_HOR * 2).toInt(),
                heightDp.toInt() + BUBBLE_INTERNAL_TEXT_SIDE_PADDING_VERT * 2,
            )
        else {
            val shiftDp = lastLineWidthDp + timeWidthDp - maxTextWidthDp - TIME_RIGHT_PADDING
            Pair(
                (maxTextWidthDp + BUBBLE_INTERNAL_TEXT_SIDE_PADDING_HOR * 2 + shiftDp).toInt(),
                heightDp.toInt() + BUBBLE_INTERNAL_TEXT_SIDE_PADDING_VERT * 2,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun toLocalDateTime(timestamp: String): LocalDateTime {
    return LocalDateTime.parse(
        "${timestamp.substring(0,20)}Z",
        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.asHhMmTime() =
    (if (this.hour < 10) "0" else "") + "${this.hour}:" + (if (this.minute < 10) "0" else "") + "${this.minute}"