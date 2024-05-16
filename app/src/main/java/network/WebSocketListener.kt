package network

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import models.user.locUser
import okhttp3.*
import org.json.JSONObject

class EchoWebSocketListener(context: Context) : WebSocketListener() {

    private val contexto = context
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i("WEB SOCKET CONNECTION", "FUCKKK")
        webSocket.send("Hello, it's me. I was wondering if after all these years you'd like to meet.")
    }


    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i("WEB SOCKET RECEIVING", text)
        val jsonObject = JSONObject(text)

        val lat = jsonObject.getString("latitude")
        val long = jsonObject.getString("longitude")
        Log.i("POSICION WEBSOCKET" ,"$lat + $long")

        val sharedPref = contexto.getSharedPreferences("miPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("latitude", lat)
        editor.putString("longitude", long)
        editor.apply()
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("Error : " + t.message)
    }

}
