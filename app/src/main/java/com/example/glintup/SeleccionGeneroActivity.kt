package com.example.glintup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.glintup.databinding.ActivitySeleccionGeneroBinding

class SeleccionGeneroActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeleccionGeneroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionGeneroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar la lista de información pasada de la actividad anterior
        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()



        configurarBotonSiguiente(nuevaInformacionList)
    }



    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {
            // Captura el género seleccionado usando el RadioGroup
            val genero = when (binding.generoGroup.checkedRadioButtonId) {
                R.id.generoHombre -> "H"
                R.id.generoMujer -> "M"
                R.id.generoOtro -> "O"
                else -> "No especificado"  // Asumiendo un caso por defecto
            }

            if (genero != "No especificado") {
                val intent = Intent(this, InteresesActivity::class.java)
                val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("genero", genero)
                    apply()
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, seleccione una opción de género.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
