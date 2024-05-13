package com.example.glintup

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
                R.id.generoHombre -> "Hombre"
                R.id.generoMujer -> "Mujer"
                R.id.generoOtro -> "Otro"
                else -> "No especificado"  // Asumiendo un caso por defecto
            }

            if (genero != "No especificado") {
                informacionList.add(genero)
                val intent = Intent(this, InteresesActivity::class.java)
                intent.putStringArrayListExtra("informacionList", informacionList)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, seleccione una opción de género.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
