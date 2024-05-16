package network

import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketClient(url: String, private val listener: WebSocketListener) {
    private var client: OkHttpClient? = null
    private var ws: WebSocket? = null

    init {
        // Configure the client
        val request = Request.Builder()
            .url(url)
            .build()

        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        // Create WebSocket using the client and request
        ws = client!!.newWebSocket(request, listener)
    }

    fun send(message: String) {
        ws?.send(message)
    }

    fun close() {
        ws?.close(1000, "Goodbye !")
    }
}
