package com.example.loginactivity

import ImagenAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginactivity.databinding.ActivityMatchBinding

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageAdapter = ImagenAdapter(getImageList())
        binding.viewPagerImages.adapter = imageAdapter
    }

    private fun getImageList(): List<Int> {

        return listOf(
            R.drawable.zac_efron,
            R.drawable.pf_pic,
            R.drawable.brad_pitt
        )
    }
}
