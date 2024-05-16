package com.example.glintup


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import com.example.glintup.databinding.ActivitySignupBinding
import models.User
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(obtenerToken()!=null){
            verifyToken()
        }
        configurarBotones()
    }

    private fun configurarBotones() {
        binding.iniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        binding.crearCuenta.setOnClickListener {
            irAIngresarNumero()
        }
    }
    private fun iniciarSesion() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }

    private fun irAIngresarNumero() {
        val intent = Intent(this, IngresarNumeroActivity::class.java)
        startActivity(intent)
    }
    private fun obtenerToken(): String? {
        val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token_jwt", null)
    }


    private fun verifyToken() {
        RetrofitClient.create(applicationContext).authorizeToken().enqueue(object
            :Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val user = response.body()
                    if(user != null){
                        val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                        sharedPreferences.edit().apply {
                            putString("nombre", user.name)
                            putString("birthdate", user.birthdate)
                            putString("genero", user.gender)
                            //putString("foto", user.profile_picture[0])
                            putString("id", user._id)
                            apply()
                            Log.i("INFO USUARIO SINGUP", user.name +" "+ user.birthdate +" "+ user.birthdate +" "+user._id)
                            Log.i("USUARIO", user.toString())
                        }
                        startActivity(Intent(baseContext, MatchActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                    sharedPreferences.edit().remove("token_jwt").apply()
                }
            })
    }
}
