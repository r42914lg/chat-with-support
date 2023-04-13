package com.r42914lg.chatsandbox.conversation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.r42914lg.chatsandbox.R
import com.r42914lg.chatsandbox.ui.theme.GreyBackground

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun UserInput(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {},
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    var textFieldFocusState by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        UserInputText(
            modifier = Modifier.weight(1f),
            textFieldValue = textState,
            onTextChanged = { textState = it },
            onTextFieldFocused = { focused ->
                if (focused) {
                    resetScroll()
                }
                textFieldFocusState = focused
            },
            focusState = textFieldFocusState
        )

        Spacer(modifier = Modifier.width(8.dp))

        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            Image(
                painterResource(if (textState.text.isEmpty()) R.drawable.chat_send_disabled else R.drawable.chat_send_enabled),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clickable {
                        if (textState.text.isNotEmpty()) {
                            onMessageSent(textState.text)
                            textState = TextFieldValue()
                            resetScroll()
                            keyboardController?.hide()
                            textFieldFocusState = false
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    modifier: Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean
) {
    Box(modifier = modifier) {

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        var lastFocusState by remember { mutableStateOf(false) }

        BasicTextField(
            value = textFieldValue,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .focusRequester(focusRequester)
                .background(
                    color = GreyBackground,
                    shape = RoundedCornerShape(20.dp)
                )
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 9.dp, bottom = 9.dp)
                .align(Alignment.CenterStart)
                .onFocusChanged { state ->
                    if (lastFocusState != state.isFocused) {
                        onTextFieldFocused(state.isFocused)
                    }
                    lastFocusState = state.isFocused
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Send
            ),
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
        )

        if (!focusState)
            focusManager.clearFocus()

        if (textFieldValue.text.isEmpty() && !focusState) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                text = stringResource(id = R.string.textfield_hint),
            )
        }
    }
}
