package network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import models.message
import models.user.locUser
import okhttp3.*
import org.json.JSONObject
import java.lang.reflect.Type

class EchoWebSocketListener2(context: Context, id:String) : WebSocketListener() {

    private val contexto = context

    object GsonUtils {
        val gson: Gson = Gson()

        inline fun <reified T : Any> fromJson(json: String): T {
            val type: Type = object : TypeToken<T>() {}.type
            return gson.fromJson(json, type)
        }
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.i("WEB SOCKET CONNECTION", "FUCKKK")
        Log.i("ON OPEN", response.body.toString())

    }

    override fun onMessage(webSocket: WebSocket, text: String) {

        val sharedPref = contexto.getSharedPreferences("lista", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("mensajes", text)
        editor.apply()

        Log.i("WEB SOCKET RECEIVING", text)

    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(1000, null)
        println("Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        println("Error : " + t.message)
    }

}
