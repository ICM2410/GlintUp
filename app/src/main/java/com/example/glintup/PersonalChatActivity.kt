package com.example.glintup

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.glintup.databinding.ActivityPersonalChatBinding
import models.messageRequest
import models.user.defaultResponse
import models.user.getImageRequest
import network.EchoWebSocketListener
import network.RetrofitClient
import network.WebSocketClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class PersonalChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPersonalChatBinding
    private lateinit var webSocketClient: WebSocketClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val nombre = intent.getStringExtra("nombre")
        val foto = intent.getStringExtra("foto")
        val chat = intent.getStringExtra("chat")

            if (chat != null) {
                Log.i("ID desde personal", chat)
            }


        binding.nombre.text = nombre


        configurarBotonSiguiente(id)
        pedirFoto(foto)

        webSocketClient = WebSocketClient("ws://ws0nr9l7-8080.use2.devtunnels.ms/api/chat/ws/${chat!!}", EchoWebSocketListener(applicationContext))


        binding.send.setOnClickListener {
            if(chat != null){
                enviarMensaje(chat)
            } else{
                Log.i("ERROR EN CHAT", "No llego el chat id")
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
