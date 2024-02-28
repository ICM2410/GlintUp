package com.example.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.loginactivity.databinding.ActivityFotosRecientesBinding

class FotosRecientesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFotosRecientesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFotosRecientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }

    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, MatchActivity::class.java)
            startActivity(intent)
        }
    }
}
