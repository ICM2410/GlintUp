package com.example.glintup

import ImagenAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.glintup.databinding.ActivityMatchBinding
import models.User

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding

    val userList = listOf(
        User("Luis", "Vera", R.drawable.zac_efron.toString(), "66392d25bc6607b1df0e2550"),
        User("Pedro", "Perez", R.drawable.pf_pic.toString(), "66392d25bc6607b1df0e2551"),
        User("Juan", "Gomez", R.drawable.brad_pitt.toString(), "66392d25bc6607b1df0e2552"),
        User("Maria", "Gonzalez", "perfil4.jpg", "66392d25bc6607b1df0e2553")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuarios = ImagenAdapter(userList)
        binding.viewPagerImages.adapter = usuarios
        binding.viewPagerImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val user = userList[position]
                binding.nombre.text = "${user.name} ${user.lastname}"
            }
        })
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

