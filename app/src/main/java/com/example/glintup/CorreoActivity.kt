package com.example.glintup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.glintup.databinding.ActivityCorreoBinding

class CorreoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCorreoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorreoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la lista de información de la actividad anterior
        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()

        configurarBotonSiguiente(nuevaInformacionList)
    }

    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {
            val correo = binding.correo.text.toString()
            if (correo.isNotEmpty()) {
                informacionList.add(correo)
                val intent = Intent(this, ContrasenaActivity::class.java)
                val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("correo", correo)
                    apply()
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, ingrese un correo electrónico.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
