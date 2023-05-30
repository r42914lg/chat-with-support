package com.r42914lg.chatsandbox

import androidx.activity.ComponentActivity
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.r42914lg.chatsandbox.conversation.ConversationContent
import com.r42914lg.chatsandbox.conversation.ConversationTestTag
import com.r42914lg.chatsandbox.conversation.ConversationUiState
import com.r42914lg.chatsandbox.conversation.initialMessages
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConversationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        // Launch the conversation screen
        composeTestRule.setContent {
            ConversationContent(
                uiState = ConversationUiState(initialMessages),
            )
        }
    }

    @Test
    fun app_launches() {
        // Check that the conversation screen is visible on launch
        composeTestRule.onNodeWithTag(ConversationTestTag).assertIsDisplayed()
    }

    @Test
    fun userScrollsUp_jumpToBottomAppears() {
        // Check list is snapped to bottom and swipe up
        findJumpToBottom().assertDoesNotExist()
        composeTestRule.onNodeWithTag(ConversationTestTag).performTouchInput {
            this.swipe(
                start = this.center,
                end = Offset(this.center.x, this.center.y + 500),
                durationMillis = 1000,
            )
        }
        // Check that the jump to bottom button is shown
        findJumpToBottom().assertDoesNotExist()
    }

    @Test
    fun jumpToBottom_snapsToBottomAndDisappears() {
        // When the scroll is not snapped to the bottom
        composeTestRule.onNodeWithTag(ConversationTestTag).performTouchInput {
            this.swipe(
                start = this.center,
                end = Offset(this.center.x, this.center.y + 500),
                durationMillis = 1004,
            )
        }
        // Snap scroll to the bottom
        findJumpToBottom().performClick()

        // Check that the button is hidden
        findJumpToBottom().assertDoesNotExist()
    }

    private fun findJumpToBottom() =
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.jumpBottom),
            useUnmergedTree = true,
        )
}
