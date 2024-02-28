package com.example.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.loginactivity.databinding.ActivityInteresesBinding

class InteresesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInteresesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInteresesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }
    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, OrientacionSexualActivity::class.java)
            startActivity(intent)
        }
    }
}
