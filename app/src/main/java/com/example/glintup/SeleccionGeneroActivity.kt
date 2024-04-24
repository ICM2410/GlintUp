package com.example.glintup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.glintup.databinding.ActivitySeleccionGeneroBinding

class SeleccionGeneroActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeleccionGeneroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionGeneroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }
    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, InteresesActivity::class.java)
            startActivity(intent)
        }
    }
}
