package com.example.glintup

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.glintup.databinding.ActivityContrasenaBinding

class ContrasenaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContrasenaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContrasenaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()

    }
    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, CumpleanosActivity::class.java)
            startActivity(intent)
        }
    }
}