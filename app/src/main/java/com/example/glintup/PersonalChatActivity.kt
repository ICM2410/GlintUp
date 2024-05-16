package com.example.glintup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.glintup.databinding.ActivityPersonalChatBinding
import models.user.getImageRequest
import network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class PersonalChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPersonalChatBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val nombre = intent.getStringExtra("nombre")
        val foto = intent.getStringExtra("foto")

        if (id != null) {
            Log.i("ID desde personal", id)
        }

        binding.nombre.text = nombre


        configurarBotonSiguiente(id)
        pedirFoto(foto)
    }

    private fun configurarBotonSiguiente(id: String?) {
        binding.mapa.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java).putExtra("id", id)
            startActivity(intent)
        }
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
