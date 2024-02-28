package com.example.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.loginactivity.databinding.ActivityNombreUsuarioBinding

class NombreUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNombreUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNombreUsuarioBinding.inflate(layoutInflater)
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
