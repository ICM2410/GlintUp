package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginactivity.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navegacion.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.likes ->{
                    startActivity(Intent(this,LikesActivity::class.java))
                    true
                }
                R.id.chat ->{
                    startActivity(Intent(this,ChatActivity::class.java))
                    true
                }

                R.id.profile->{

                    true
                }

                R.id.lamp->{

                    true
                }

                R.id.explore->{

                    true
                }

                else ->{
                    false
                }
            }

        }

        binding.edit.setOnClickListener{
            var intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

    }
}