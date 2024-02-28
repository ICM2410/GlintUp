package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginactivity.databinding.ActivityExploreBinding

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navegacion.setOnItemSelectedListener {
            navigateToItem(it.itemId)
        }
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

}