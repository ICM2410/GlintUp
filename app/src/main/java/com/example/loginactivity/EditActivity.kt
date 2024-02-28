package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginactivity.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {
    private lateinit var binding :ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.modifyPInfo.setOnClickListener {
            var intent = Intent(this, SeleccionGeneroActivity::class.java)
            startActivity(intent)
        }
    }
}