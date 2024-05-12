package com.example.glintup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.glintup.databinding.ActivityCorreoBinding

class CorreoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCorreoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorreoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()

    }
    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, ContrasenaActivity::class.java)
            startActivity(intent)
        }
    }
}