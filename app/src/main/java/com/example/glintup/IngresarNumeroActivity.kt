package com.example.glintup

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
                val informacionLista = mutableListOf<String>(numeroTexto)
                intent.putExtra("informacionLista", ArrayList(informacionLista))
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, ingrese un número válido.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
