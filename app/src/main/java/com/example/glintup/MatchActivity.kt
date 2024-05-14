package com.example.glintup

import ImagenAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.glintup.databinding.ActivityMatchBinding
import models.LoginResponse
import models.RegisterRequest
import models.User
import models.User_list
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding

    private val immutableList = listOf("Apple", "Banana", "Orange")

    val userList = listOf(
        User("Veronica", "12/12/2002", "F", "F", immutableList, immutableList),
        User("Luis", "12/12/2002", "M", "F", immutableList, immutableList),
        User("Pedro", "12/12/2002", "M", "F", immutableList, immutableList),
        User("Fucker", "12/12/2002", "M", "F", immutableList, immutableList)
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
                binding.nombre.text = "${user.name}"
            }
        })

        getUsers()

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


    private fun getUsers() {

        RetrofitClient.create(applicationContext).getUsersOnProximity().enqueue(object :
            Callback<User_list> {
            override fun onResponse(call: Call<User_list>, response: Response<User_list>) {
                if(response.isSuccessful){
                    Log.i("Test and sucess", response.body().toString())
                }else{
                    Toast.makeText(this@MatchActivity, "Server Internal Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User_list>, t: Throwable) {
                Toast.makeText(this@MatchActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

