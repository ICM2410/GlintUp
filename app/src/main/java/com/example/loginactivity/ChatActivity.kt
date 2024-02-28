package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginactivity.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
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



    }
}