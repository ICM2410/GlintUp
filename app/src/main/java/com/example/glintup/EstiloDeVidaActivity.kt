package com.example.glintup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.glintup.databinding.ActivityEstiloDeVidaBinding
import models.LoginResponse
import models.RegisterRequest
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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

                val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("hobbies", respuestas.toString())
                    apply()
                }
                processInformation(informacionList)
            } else {
                Toast.makeText(this, "Por favor, responda todas las preguntas antes de continuar.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun processInformation(informacionList: ArrayList<String>) {
        // Suponiendo que cada entrada en la lista corresponde a un campo específico

        val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)

        val numero = sharedPreferences.getString("numero", null)
        val phone = sharedPreferences.getString("numero", null)
        val nombre = sharedPreferences.getString("nombre", null)
        val correo = sharedPreferences.getString("correo", null)
        val contrasena = sharedPreferences.getString("password", null)
        val fecha = sharedPreferences.getString("birthdate", null)
        val genero = sharedPreferences.getString("genero", null)
        val generoPref = sharedPreferences.getString("preferencias", null)
        val kmCercania = sharedPreferences.getString("distancia", null)
        val habitos = sharedPreferences.getString("hobbies", null)
        val parsedhobbies = parseHobbies(habitos.toString())


        Log.i("FINAL", numero.toString())
        Log.i("FINAL", nombre.toString())
        Log.i("FINAL", correo.toString())
        Log.i("FINAL", contrasena.toString())
        Log.i("FINAL", genero.toString())
        Log.i("FINAL", generoPref.toString())
        Log.i("FINAL", kmCercania.toString())
        Log.i("FINAL", habitos.toString())

        if (kmCercania != null) {
            register(nombre.toString(), correo.toString(), contrasena.toString(), phone.toString(), fecha.toString(), genero.toString(), generoPref.toString(), kmCercania.toInt(), parsedhobbies)
        }
    }

    fun parseHobbies(input: String): List<Map<String, Any>> {
        val hobbiesList = mutableListOf<Map<String, Any>>()

        // Dividir el input por comas para separar cada hobby
        val hobbies = input.split(",").map { it.trim() }

        hobbies.forEach { hobby ->
            // Dividir cada hobby en clave y valor
            val parts = hobby.split(":").map { it.trim() }
            if (parts.size == 2) {
                // Agregar al resultado como un mapa de clave "name" y "description"
                hobbiesList.add(mapOf("name" to parts[0], "description" to parts[1]))
            }
        }

        return hobbiesList
    }

    private fun register(name: String, email:String, password: String, phone: String, birthdate: String, gender: String, preferences: String, prefered_distance: Int, hobbies: List<Map<String, Any>>) {
        val registerRequest = RegisterRequest(name, email, password, phone, birthdate, gender, preferences, prefered_distance, hobbies)

        RetrofitClient.create(applicationContext).registerUser(registerRequest).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>){
                if(response.isSuccessful){
                    val token = response.body()?.token
                    Log.i("AUTH TOKEN", token.toString())
                    if(token != null){
                        guardarToken(token)

                        val intent = Intent(baseContext, FotosRecientesActivity::class.java)
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(this@EstiloDeVidaActivity, "Server Internal Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@EstiloDeVidaActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guardarToken(token: String) {
        val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("token_jwt", token)
            apply()
        }
    }


    private fun logInformacionRecibida(informacionList: ArrayList<String>) {
        Log.d("EstiloDeVidaActivity", "Información actual en la lista:")
        informacionList.forEachIndexed { index, info ->
            Log.d("EstiloDeVidaActivity", "Elemento $index: $info")
        }
    }
}
