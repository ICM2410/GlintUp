package com.example.glintup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.glintup.databinding.ActivityPersonalChatBinding

class PersonalChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPersonalChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }

    private fun configurarBotonSiguiente() {
        binding.mapa.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }
}