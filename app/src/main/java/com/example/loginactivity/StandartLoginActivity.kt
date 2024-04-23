package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.loginactivity.databinding.ActivityStandartLoginBinding
import org.apache.commons.lang3.ObjectUtils.Null

class StandartLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStandartLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStandartLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var correo: String
        var contra: String

        binding.iniciarSesion.setOnClickListener {
            correo = binding.editTextTextEmailAddress.text.toString()
            contra = binding.editTextTextPassword.text.toString()

            if(correo.isNotEmpty()  && contra.isNotEmpty()){
                startActivity(Intent(this,MatchActivity::class.java) )
                Log.i("DATA", correo)
                Log.i("DATA", contra)
            }
            else{
                Toast.makeText(this, "Ingresa los datos para iniciar sesion", Toast.LENGTH_SHORT).show()
            }
        }

    }

}