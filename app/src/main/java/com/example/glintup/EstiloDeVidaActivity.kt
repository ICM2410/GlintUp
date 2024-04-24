package com.example.glintup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.glintup.databinding.ActivityEstiloDeVidaBinding

class EstiloDeVidaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEstiloDeVidaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEstiloDeVidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }
    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, FotosRecientesActivity::class.java)
            startActivity(intent)
        }
    }
}
