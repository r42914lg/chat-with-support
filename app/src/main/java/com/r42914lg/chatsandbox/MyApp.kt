package com.r42914lg.chatsandbox

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import java.net.URI
import java.net.URISyntaxException

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        _socket = try {

            val uri = URI.create(ROOT_CHAT)
            val options = IO.Options.builder()
                .setPath(PATH_CHAT)
                .setTransports(arrayOf(WebSocket.NAME))
                .setQuery("token=user_token")
                .build()

            IO.socket(uri, options)

        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    private var _socket: Socket? = null
    val socket: Socket?
        get() = _socket

    companion object {
        const val ROOT_CHAT = "https://prod.chat.com/chat"
        const val PATH_CHAT = "/messaging/socket.io"
    }

}