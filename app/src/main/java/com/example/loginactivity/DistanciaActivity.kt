package com.example.loginactivity
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginactivity.databinding.ActivityDistanciaBinding

class DistanciaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDistanciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDistanciaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.kilometros.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Actualiza el valor del TextView con el valor seleccionado del SeekBar
                "$progress km".also { binding.kilometrosValor.text = it }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementarla, sin embargo es obligatoria
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // No es necesario implementarla, sin embargo es obligatoria
            }
        })
    }
}