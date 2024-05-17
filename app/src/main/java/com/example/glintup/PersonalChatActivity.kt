package com.example.glintup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import classes.ChatAdapter
import com.bumptech.glide.Glide
import com.example.glintup.databinding.ActivityPersonalChatBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import models.message
import models.messageRequest
import models.user.defaultResponse
import models.user.getImageRequest
import network.EchoWebSocketListener2
import network.RetrofitClient
import network.WebSocketClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.lang.reflect.Type
import java.time.LocalDate

class PersonalChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPersonalChatBinding
    private lateinit var webSocketClient: WebSocketClient
    private val messages = mutableListOf<message>()

    private var adapter: ChatAdapter? = null

    object GsonUtils {
        val gson: Gson = Gson()

        inline fun <reified T : Any> fromJson(json: String): T {
            val type: Type = object : TypeToken<T>() {}.type
            return gson.fromJson(json, type)
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            // Tu código que deseas ejecutar cada 10 segundos
            ejecutarFuncionPeriodica()

            // Vuelve a ejecutar el Runnable después de 10 segundos
            handler.postDelayed(this, 2000)
        }
    }

    private fun ejecutarFuncionPeriodica() {

        val prefs = applicationContext.getSharedPreferences("lista", Context.MODE_PRIVATE)

        val mensajes = prefs.getString("mensajes",null)

        if(!mensajes.isNullOrEmpty()){
            messages.add(message("x", "Hola", LocalDate.now().toString()))
            adapter!!.notifyItemInserted(messages.size - 1)
            val message: List<message> = GsonUtils.fromJson(mensajes)
            messages.clear()
            messages.addAll(message)
            adapter!!.notifyDataSetChanged()
            binding.mensajes.scrollToPosition(messages.size - 1)

        }

        //Log.i("MENSAJES", mensajes.toString())


        Log.d("PeriodicTask", "Funcion ejecutada")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler.post(runnable)

        val id = intent.getStringExtra("id")
        val nombre = intent.getStringExtra("nombre")
        val foto = intent.getStringExtra("foto")
        val chat = intent.getStringExtra("chat")

        if (chat != null) {
            Log.i("ID desde personal", chat)
        }


        adapter = ChatAdapter(messages,id!!)

        binding.mensajes.layoutManager = LinearLayoutManager(this)
        binding.mensajes.adapter = adapter


        binding.nombre.text = nombre


        configurarBotonSiguiente(id)
        pedirFoto(foto)

        webSocketClient = WebSocketClient("ws://ws0nr9l7-8080.use2.devtunnels.ms/api/chat/ws/${chat!!}", EchoWebSocketListener2(applicationContext, id))



        binding.send.setOnClickListener {
            val messageContent = binding.msg.text.toString()
            if (messageContent.isNotBlank()) {
                enviarMensaje(chat)
                val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                    Date()
                )
                messages.add(message(id, messageContent, timestamp))
                adapter!!.notifyItemInserted(messages.size - 1)
                binding.mensajes.scrollToPosition(messages.size - 1)
                binding.msg.text.clear()
            }
        }

    }





    private fun configurarBotonSiguiente(id: String?) {
        binding.mapa.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java).putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun enviarMensaje(id:String) {
        val text = binding.msg.text
        val requestmessage = messageRequest(id, text.toString())
        RetrofitClient.create(applicationContext).createMessage(requestmessage).enqueue(object :
        Callback<defaultResponse> {
            override fun onResponse(
                call: Call<defaultResponse>,
                response: Response<defaultResponse>
            ) {
                Log.i("MESSAGE CREATION", response.body().toString())
            }

            override fun onFailure(call: Call<defaultResponse>, t: Throwable) {
                Toast.makeText(this@PersonalChatActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun pedirFoto(id:String?){

         val contexto = applicationContext

        val idImg = getImageRequest(id!!)
        RetrofitClient.create(this).fetchImage(idImg).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        // Create a temporary file in your application's cache directory

                        val imageFile = File(contexto.cacheDir, "temp_$id.jpg")
                        var outputStream: FileOutputStream? = null
                        try {
                            outputStream = FileOutputStream(imageFile)
                            outputStream.write(responseBody.bytes())
                        } catch (e: Exception) {
                            Log.e("IMAGE", "Error writing to file", e)
                        } finally {
                            outputStream?.close()
                        }
                        Glide.with(contexto)
                            .load(imageFile)
                            .into(binding.imageView6)

                        // Optionally, delete the file after Glide has done loading it
                        imageFile.deleteOnExit()
                        Log.i("RESPONSE FROM IMAGE", "SUCCESSS FUCKERRR SHIT")
                    }
                } else {
                    Log.i("RESPONSE FROM IMAGE", "FAILURE")

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.i("RESPONSE FROM IMAGE", "SERVER IMAGE ISSUE")
            }
        })

    }
}
