package com.example.glintup


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.glintup.databinding.ActivitySignupBinding

class  SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(obtenerToken()!=null){
            startActivity(Intent(this, MatchActivity::class.java))
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
    // LogInActivity estoooo
    private fun iniciarSesion() {
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }

    private fun irAIngresarNumero() {
        val intent = Intent(this, MatchActivity::class.java)
        startActivity(intent)
    }
 //IngresarNumeroActivity
    private fun obtenerToken(): String? {
        val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token_jwt", null)
    }
}
