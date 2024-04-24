package com.example.glintup

import ImagenAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.glintup.databinding.ActivityMatchBinding

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageAdapter = ImagenAdapter(getImageList())
        binding.viewPagerImages.adapter = imageAdapter


        binding.navegacion.setOnItemSelectedListener {
            navigateToItem(it.itemId)

        }
    }

    override fun onResume() {
        super.onResume()
        binding.navegacion.menu.getItem(0).isChecked = true
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
            R.id.explore -> {
                startActivity(Intent(this, ExploreActivity::class.java))
                return true
            }
            else -> {
                return false
            }
        }
    }


    private fun getImageList(): List<Int> {

        return listOf(
            R.drawable.zac_efron,
            R.drawable.pf_pic,
            R.drawable.brad_pitt
        )
    }
}

