package com.example.glintup

import ImagenAdapter
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.glintup.databinding.ActivityMatchBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import models.User
import models.user.locationRequest
import kotlin.math.log

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    private val locationPermissionName = Manifest.permission.ACCESS_FINE_LOCATION
    private lateinit var location: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback { isGranted ->
            if (isGranted) {
                setupLocation()
            }
        })

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
        binding.viewPagerImages.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val user = userList[position]
                binding.nombre.text = "${user.name} ${user.lastname}"
            }
        })

        setupLocation()
        setupPermissionRequest()
        startLocationUpdates()

        binding.navegacion.setOnItemSelectedListener {
            navigateToItem(it.itemId)
        }
    }

    override fun onResume() {
        super.onResume()

        setupLocation()
        setupPermissionRequest()
        startLocationUpdates()

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


    //---------------Permisos-------------------------------------//


    private fun setupPermissionRequest() {
        permissionRequest.launch(locationPermissionName)
    }



    private fun setupLocation() {
        location = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = createLocationRequest()
        locationCallback = createLocationCallback()
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 60 * 1000 // Intervalo de 10 minutos (en milisegundos)
        locationRequest.fastestInterval = 5 * 60 * 1000 // Actualización más rápida cada 5 minutos (en milisegundos)
        return locationRequest
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.locations.forEach { location ->
                    lastLocation = location
                    Log.i("LOCATION", "Coords: ${lastLocation?.latitude}, ${lastLocation?.longitude} ")
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, locationPermissionName) == PackageManager.PERMISSION_GRANTED) {
            location.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }


}

