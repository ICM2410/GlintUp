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


            if (ejercicioRespuesta.isNotEmpty() && fumarRespuesta.isNotEmpty() && leerRespuesta.isNotEmpty()) {
                respuestas.add("Ejercicio: $ejercicioRespuesta")
                respuestas.add("Fumar: $fumarRespuesta")
                respuestas.add("Leer: $leerRespuesta")
                informacionList.add(respuestas.joinToString(", "))

                processInformation(informacionList)
                val intent = Intent(this, FotosRecientesActivity::class.java)
                intent.putStringArrayListExtra("informacionList", informacionList)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, responda todas las preguntas antes de continuar.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun processInformation(informacionList: ArrayList<String>) {
        // Suponiendo que cada entrada en la lista corresponde a un campo específico
        val numero = informacionList[0]
        val nombre = informacionList[1]
        val correo = informacionList[2]
        val contrasena = informacionList[3]
        val fecha = informacionList[4]
        val genero = informacionList[5]
        val orientacionSexual = informacionList[6].split(", ")
        val generoPref = informacionList[7].split(", ")
        val kmCercania = informacionList[8]
        val habitos = informacionList[9]

        // Imprimir todos los detalles en el log para verificación
        Log.d("FinalData", "Número: $numero")
        Log.d("FinalData", "Nombre: $nombre")
        Log.d("FinalData", "Correo: $correo")
        Log.d("FinalData", "Contraseña: $contrasena")
        Log.d("FinalData", "Fecha: $fecha")
        Log.d("FinalData", "Género: $genero")
        Log.d("FinalData", "Orientación sexual: ${orientacionSexual.joinToString(", ")}")
        Log.d("FinalData", "Género de preferencia: ${generoPref.joinToString(", ")}")
        Log.d("FinalData", "Kilómetros de cercanía: $kmCercania")
        Log.d("FinalData", "Hábitos: $habitos")
    }




    private fun logInformacionRecibida(informacionList: ArrayList<String>) {
        Log.d("EstiloDeVidaActivity", "Información actual en la lista:")
        informacionList.forEachIndexed { index, info ->
            Log.d("EstiloDeVidaActivity", "Elemento $index: $info")
        }
    }
}
