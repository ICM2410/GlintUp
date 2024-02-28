package com.example.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.loginactivity.databinding.ActivityIngresarNumeroBinding

class IngresarNumeroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngresarNumeroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngresarNumeroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }

    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, NombreUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}
