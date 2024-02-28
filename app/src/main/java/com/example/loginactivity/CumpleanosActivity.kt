package com.example.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.loginactivity.databinding.ActivityCumpleanosBinding

class CumpleanosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCumpleanosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCumpleanosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }

    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, SeleccionGeneroActivity::class.java)
            startActivity(intent)
        }
    }
}
