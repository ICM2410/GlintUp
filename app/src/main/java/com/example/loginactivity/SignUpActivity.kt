package com.example.loginactivity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.loginactivity.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val intent = Intent(this, IngresarNumeroActivity::class.java)
        startActivity(intent)
    }
}
