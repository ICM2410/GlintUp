package com.example.glintup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.SeekBar
import com.example.glintup.databinding.ActivityDistanciaBinding

class DistanciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDistanciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarSeekBar()
        configurarBotonSiguiente()
    }

    private fun configurarSeekBar() {
        binding.kilometros.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                actualizarValorSeekBar(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementarla, sin embargo es obligatoria
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementarla, sin embargo es obligatoria
            }
        })
    }

    private fun actualizarValorSeekBar(progress: Int) {
        binding.kilometrosValor.text = "$progress km"
    }

    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, EstiloDeVidaActivity::class.java)
            startActivity(intent)
        }
    }
}
