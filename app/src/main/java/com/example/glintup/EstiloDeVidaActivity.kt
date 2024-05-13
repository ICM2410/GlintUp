package com.example.glintup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.glintup.databinding.ActivityEstiloDeVidaBinding

class EstiloDeVidaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEstiloDeVidaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstiloDeVidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()

        configurarBotonSiguiente(nuevaInformacionList)
        logInformacionRecibida(nuevaInformacionList)
    }

    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {
            val respuestas = ArrayList<String>()

            // Recoger información de los RadioGroups
            val ejercicioRespuesta = when (binding.grupoEjercicio.checkedRadioButtonId) {
                R.id.frecuencia1Buton1 -> "Todos los días"
                R.id.frecuencia2Buton1 -> "A menudo"
                R.id.frecuencia3Buton3 -> "Algunas veces"
                R.id.frecuencia4Buton4 -> "Nunca"
                else -> ""
            }

            val fumarRespuesta = when (binding.grupoFumar.checkedRadioButtonId) {
                R.id.fumadorSocial -> "Fumador social"
                R.id.fumadorAlBeber -> "Fumador al beber"
                R.id.noFumador -> "No fumador"
                R.id.fumador -> "Fumador"
                R.id.intentoDejarlo -> "Intento dejarlo"
                else -> ""
            }

            val leerRespuesta = when (binding.grupoLeer.checkedRadioButtonId) {
                R.id.ejercicioTodosLosDias -> "Todos los días"
                R.id.ejercicioAMenudo -> "A menudo"
                R.id.ejercicioAlgunasVeces -> "Algunas veces"
                R.id.ejercicioNunca -> "Nunca"
                else -> ""
            }

            // Añadir las respuestas a la lista temporal solo si ambas preguntas han sido contestadas
            if (ejercicioRespuesta.isNotEmpty() && fumarRespuesta.isNotEmpty() && leerRespuesta.isNotEmpty()) {
                respuestas.add("Ejercicio: $ejercicioRespuesta")
                respuestas.add("Fumar: $fumarRespuesta")
                respuestas.add("Leer: $leerRespuesta")
                informacionList.add(respuestas.joinToString(", "))

                val intent = Intent(this, FotosRecientesActivity::class.java)
                intent.putStringArrayListExtra("informacionList", informacionList)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, responda todas las preguntas antes de continuar.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun logInformacionRecibida(informacionList: ArrayList<String>) {
        Log.d("EstiloDeVidaActivity", "Información actual en la lista:")
        informacionList.forEachIndexed { index, info ->
            Log.d("EstiloDeVidaActivity", "Elemento $index: $info")
        }
    }
}
