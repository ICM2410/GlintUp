package com.example.glintup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.example.glintup.databinding.ActivityIngresarNumeroBinding

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
            val numeroTexto = binding.numero.text.toString()
            if (numeroTexto.isNotEmpty()) {
                val intent = Intent(this, NombreUsuarioActivity::class.java)
                val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("numero", numeroTexto)
                    apply()
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, ingrese un número válido.", Toast.LENGTH_LONG).show()
            }
        }

        binding.back.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }


}
