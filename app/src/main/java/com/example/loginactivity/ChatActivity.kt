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
            navigateToItem(it.itemId)
        }

        configurarBotonSiguiente()

    }

    private fun navigateToItem(itemId: Int): Boolean {
        when (itemId) {
            R.id.likes -> {
                startActivity(Intent(this, LikesActivity::class.java))
                return true
            }
            R.id.chat -> {
                startActivity(Intent(this, ChatActivity::class.java))
                return true
            }
            R.id.profile -> {
                startActivity(Intent(this, UserActivity::class.java))
                return true
            }
            R.id.lamp -> {
                startActivity(Intent(this, MatchActivity::class.java))
                return true
            }
            R.id.explore -> {
                startActivity(Intent(this, ExploreActivity::class.java))
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun configurarBotonSiguiente() {
        binding.bradpi.setOnClickListener {
            val intent = Intent(this, PersonalChatActivity::class.java)
            startActivity(intent)
        }
    }
}
