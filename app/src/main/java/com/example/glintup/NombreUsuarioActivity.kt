package com.example.glintup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.example.glintup.databinding.ActivityNombreUsuarioBinding

class NombreUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNombreUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNombreUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionLista")
        val nuevaInformacionList = informacionLista ?: ArrayList()

        configurarBotonSiguiente(nuevaInformacionList)
    }

    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {
            val nombre = binding.nombre.text.toString()
            if (nombre.isNotEmpty()) {
                informacionList.add(nombre)
                val intent = Intent(this, CorreoActivity::class.java)
                val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("nombre", nombre)
                    apply()
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, ingrese un nombre.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
