package com.example.glintup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.glintup.databinding.ActivityContrasenaBinding

class ContrasenaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContrasenaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()

        configurarBotonSiguiente(nuevaInformacionList)
    }

    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {
            val contrasena = binding.contra.text.toString()
            if (contrasena.isNotEmpty()) {
                informacionList.add(contrasena)
                val intent = Intent(this, CumpleanosActivity::class.java)
                intent.putStringArrayListExtra("informacionList", informacionList)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, ingrese una contraseña.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
