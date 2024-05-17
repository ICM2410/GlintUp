package com.example.glintup

import ImagenAdapter
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.math.log

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    private val locationPermissionName = Manifest.permission.ACCESS_FINE_LOCATION

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val NotificationPermissionName = Manifest.permission.POST_NOTIFICATIONS

    private lateinit var location: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null
    private var respuesta: List<User>? = null

    private val CHANNEL_ID = "channel_id"
    private val notificacionesid = 101

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback { isGranted ->
            if (isGranted) {
                setupLocation()
            }
        })

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        createNotificationChannel()
        setupLocation()
        setupPermissionRequest()
        startLocationUpdates()

        getUsers()
        binding.navegacion.setOnItemSelectedListener {
            navigateToItem(it.itemId)
        }


    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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



    //----------------------------Notificaciones--------------------------------------------------------//


    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Titulo"
            val descripcion = "Notificacion"
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val channel =  NotificationChannel(CHANNEL_ID,name,importancia).apply {
                description = descripcion
            }
            val notificationManager : NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){

        val intent = Intent(this,ChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE )

        val builder = NotificationCompat.Builder(this , CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("HAZ HECHO MATCH")
            .setContentText("Felecitaciones")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this)){
            notify(notificacionesid, builder.build())
        }

        Log.i("NOTIFICACION", "Notificacion desplegada")

    }


    ///------------------------------Calculo de edad-----------------------------------------------------///


    fun calculateAge(birthdate: String): Int {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val birthDate = LocalDate.parse(birthdate, formatter)
        val currentDate = LocalDate.now()
        return Period.between(birthDate, currentDate).years
    }


    //---------------Permisos-------------------------------------//


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupPermissionRequest() {
        permissionRequest.launch(locationPermissionName)
        permissionRequest.launch(NotificationPermissionName)
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
                    val verify = response.body()
                    Log.i("LIKE", response.body().toString())
                    if (verify != null) {
                        if(verify.message == "Match created!"){
                            sendNotification()
                        }
                    }
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

                    if(respuesta?.isEmpty() == true) {
                        binding.nombre.text = "No hay usuarios disponibles en este momento"
                        binding.info.text = "Sad"
                        binding.nombre.setTextColor(Color.BLACK)
                        binding.info.setTextColor(Color.BLACK)
                        binding.botones.visibility = View.INVISIBLE
                    }

                    binding.viewPagerImages.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)

                            val user = respuesta?.get(position)
                            if (user != null) {
                                binding.nombre.text = user.name + " Edad: ${calculateAge(user.birthdate)} años"
                                binding.info.text = if (user.gender == "H") {
                                    "Género: Masculino  "
                                } else {
                                    "Género: Femenino"
                                }

                                binding.btnlike.setOnClickListener {
                                    handleLike(user._id, true)
                                    // Avanzar al siguiente usuario
                                    val nextItem = position + 1
                                    if (nextItem < respuesta?.size!!) {
                                        binding.viewPagerImages.setCurrentItem(nextItem, true)
                                    }
                                }
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

