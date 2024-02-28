package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginactivity.databinding.ActivityLoginBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotonSiguiente()
    }
    private fun configurarBotonSiguiente() {
        binding.google.setOnClickListener {
            val intent = Intent(this, MatchActivity::class.java)
            startActivity(intent)
        }
    }
}