package com.r42914lg.chatsandbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private var _socket: Socket? = null

    private val onNewMessage =
        Emitter.Listener { args ->
            runOnUiThread {
                // parse args, process message from socket
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        _socket?.disconnect()
        _socket?.off("message", onNewMessage)
    }

    fun sendMessageToSocket(text: String) {
        _socket?.emit("message", text)
    }

    private fun initSocket() {
        _socket = (application as MyApp).socket
        if (_socket == null)
            return

        _socket!!.on("message", onNewMessage)
        _socket!!.connect()

        lifecycleScope.launch {
            var waitTime = 0
            while (!_socket!!.connected() && waitTime < SOCKET_CONNECT_LIMIT) {
                delay(100)
                waitTime += 100
            }

            if (_socket!!.connected())
            // connected
            else {
                // not connected
            }
        }
    }

    companion object {
        const val SOCKET_CONNECT_LIMIT = 1000
    }
}
