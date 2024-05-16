package com.example.glintup

import ImagenAdapter
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
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
import models.likeRequest
import models.matchResponse
import models.proximityResponse
import models.user.defaultResponse
import models.user.locationRequest
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    private val locationPermissionName = Manifest.permission.ACCESS_FINE_LOCATION
    private lateinit var location: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null
    private var respuesta: List<User>? = null

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback { isGranted ->
            if (isGranted) {
                setupLocation()
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setupLocation()
        setupPermissionRequest()
        startLocationUpdates()

        getUsers()
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
                    updateLocation(lastLocation!!.latitude, lastLocation!!.longitude)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, locationPermissionName) == PackageManager.PERMISSION_GRANTED) {
            location.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    //------------------------------------------------Usuarios---------------------------------------------------//


    private fun updateLocation(lat: Double, long: Double) {
        val locationUpdate = locationRequest(
            models.user.locationRequest.Location(
                coordinates = listOf(lat, long)
            )
        )
        RetrofitClient.create(applicationContext).updateUserLocation(locationUpdate).enqueue(object :
            Callback<defaultResponse> {
            override fun onResponse(
                call: Call<defaultResponse>,
                response: Response<defaultResponse>
            ) {
                if(response.isSuccessful){
                    Log.i("Test and sucess", response.body().toString())
                }else{
                    Toast.makeText(this@MatchActivity, "Server Internal Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<defaultResponse>, t: Throwable) {
                Toast.makeText(this@MatchActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleLike(id: String, like: Boolean){
        val likerequest = likeRequest(id, like)
        RetrofitClient.create(applicationContext).createLikeAndCheckIfMatch(likerequest).enqueue(object :
        Callback<matchResponse> {
            override fun onResponse(call: Call<matchResponse>, response: Response<matchResponse>) {
                if(response.isSuccessful){
                    Log.i("LIKE", response.body().toString())
                }
            }
            override fun onFailure(call: Call<matchResponse>, t: Throwable) {
                Toast.makeText(this@MatchActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUsers() {
        RetrofitClient.create(applicationContext).getUsersOnProximity().enqueue(object :
            Callback<proximityResponse> {
            override fun onResponse(call: Call<proximityResponse>, response: Response<proximityResponse>) {
                if(response.isSuccessful){
                    respuesta = response.body()?.users
                    Log.i("Test and sucess FUCKER", respuesta.toString())

                    val usuarios = respuesta?.let { ImagenAdapter(it, this@MatchActivity) }
                    binding.viewPagerImages.adapter = usuarios

                    binding.viewPagerImages.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)

                            val user = respuesta?.get(position)
                            if (user != null) {
                                binding.nombre.text = user.name
                                if(user.gender == "H"){
                                    binding.info.text = "Genero: Masculino"
                                }
                                else {
                                    binding.info.text = "Genero: Femenino"
                                }
                                binding.btnlike.setOnClickListener {
                                    handleLike(user._id, true)
                                }
                            }
                            else{
                                binding.nombre.text = "No hay usuario disponibles en este momento"
                                binding.info.text = "Sad"
                                binding.nombre.setTextColor(Color.BLACK)
                                binding.info.setTextColor(Color.BLACK)
                            }
                        }
                    })
                }else{
                    Log.i("Tehf CUKER IS NOT DONE", "FUCKKKKKK")
                    Toast.makeText(this@MatchActivity, "Server Internal Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<proximityResponse>, t: Throwable) {
                Toast.makeText(this@MatchActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

