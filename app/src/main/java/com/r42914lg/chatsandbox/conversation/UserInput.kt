package com.r42914lg.chatsandbox.conversation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.r42914lg.chatsandbox.R

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

    Row(modifier = modifier) {
        UserInputText(
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

        // Send button
        Button(
            modifier = Modifier.height(36.dp),
            enabled = textState.text.isNotBlank(),
            onClick = {
                onMessageSent(textState.text)
                textState = TextFieldValue()
                resetScroll()
                keyboardController?.hide()
            },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                stringResource(id = R.string.send),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    textFieldValue: TextFieldValue,
    onTextFieldFocused: (Boolean) -> Unit,
    focusState: Boolean
) {
    Box(
        modifier = Modifier
            .height(64.dp)
            .width(200.dp)
    ) {
        var lastFocusState by remember { mutableStateOf(false) }
        BasicTextField(
            value = textFieldValue,
            onValueChange = { onTextChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp)
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
            maxLines = 1,
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
        )

        if (textFieldValue.text.isEmpty() && !focusState) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 32.dp),
                text = stringResource(id = R.string.textfield_hint),
            )
        }
    }
}
