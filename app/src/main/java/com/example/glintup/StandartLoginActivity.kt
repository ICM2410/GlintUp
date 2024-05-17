package com.example.glintup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.glintup.databinding.ActivityStandartLoginBinding
import models.LoginRequest
import models.LoginResponse
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StandartLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStandartLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStandartLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var correo: String
        var contra: String

        binding.iniciarSesion.setOnClickListener {
            correo = binding.editTextTextEmailAddress.text.toString()
            contra = binding.editTextTextPassword.text.toString()

            if(correo.isNotEmpty()  && contra.isNotEmpty()){
                //startActivity(Intent(this,MatchActivity::class.java) )
                Log.i("DATA", correo)
                Log.i("DATA", contra)
                iniciar_sesion(correo, contra)
            }
            else{
                Toast.makeText(this, "Ingresa los datos para iniciar sesion", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun iniciar_sesion(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        RetrofitClient.create(applicationContext).loginUser(loginRequest).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, manejar el token de JWT aquí
                    val token = response.body()?.token
                    Log.i("THIS IS MF TOKEN", response.body().toString())
                    Log.i("AUTH TOKEN", token.toString())
                    if (token != null) {
                        guardarToken(token)
                        startActivity(Intent(baseContext, MatchActivity::class.java))
                    }
                } else {
                    // Manejar el caso en que la respuesta no sea exitosa (p. ej., credenciales incorrectas)
                    Toast.makeText(this@StandartLoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Manejar el caso de fallo en la llamada a la API (p. ej., sin conexión a internet)
                Toast.makeText(this@StandartLoginActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guardarToken(token: String) {
        val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("token_jwt", token)
            apply()
        }
    }

}