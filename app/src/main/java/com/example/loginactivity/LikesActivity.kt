package com.example.loginactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.loginactivity.databinding.ActivityLikesBinding

class LikesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLikesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navegacion.menu.getItem(2).isChecked = true

        binding.navegacion.setOnItemSelectedListener {
            navigateToItem(it)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.navegacion.menu.getItem(2).isChecked = true
    }

    private fun navigateToItem(item: MenuItem): Boolean {
        when (item.itemId) {
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