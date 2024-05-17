package com.example.glintup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.SeekBar
import com.example.glintup.databinding.ActivityDistanciaBinding

class DistanciaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDistanciaBinding
    private var distanciaSeleccionada = 50  // Valor inicial del SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()

        configurarSeekBar(nuevaInformacionList)
        configurarBotonSiguiente(nuevaInformacionList)

    }


    private fun configurarSeekBar(informacionList: ArrayList<String>) {
        binding.kilometros.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                actualizarValorSeekBar(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementarla, sin embargo es obligatoria
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                distanciaSeleccionada = binding.kilometros.progress
                informacionList.add("$distanciaSeleccionada")
            }
        })
        actualizarValorSeekBar(distanciaSeleccionada)
    }

    private fun actualizarValorSeekBar(progress: Int) {
        binding.kilometrosValor.text = "$progress km"
    }

    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, EstiloDeVidaActivity::class.java)
            val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("distancia", distanciaSeleccionada.toString())
                apply()
            }
            startActivity(intent)
        }
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
