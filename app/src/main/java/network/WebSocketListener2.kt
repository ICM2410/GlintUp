package network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import models.user.locUser
import okhttp3.*
import org.json.JSONObject

class EchoWebSocketListener2(context: Context) : WebSocketListener() {

    private val contexto = context
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i("WEB SOCKET CONNECTION", "FUCKKK")
        webSocket.send("Hello, it's me. I was wondering if after all these years you'd like to meet.")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i("WEB SOCKET RECEIVING", text)
        val jsonObject = JSONObject(text)
        Log.i("Json transformation", jsonObject.toString())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("Error : " + t.message)
    }

}
