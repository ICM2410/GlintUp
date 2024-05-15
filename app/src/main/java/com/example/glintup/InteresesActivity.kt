package com.example.glintup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.glintup.databinding.ActivityInteresesBinding

class InteresesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInteresesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInteresesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()


        configurarBotonSiguiente(nuevaInformacionList)
    }



    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {

            val interes = when (binding.generoGroup.checkedRadioButtonId) {
                R.id.generoHombre -> "H"
                R.id.generoMujer -> "M"
                else -> {
                    Toast.makeText(this, "Por favor, seleccione una opción de interés.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val intent = Intent(this, OrientacionSexualActivity::class.java)
            val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("preferencias", interes)
                apply()
            }
            startActivity(intent)
        }
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
