package com.example.glintup

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.glintup.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*
import com.google.android.gms.maps.model.PolylineOptions
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.Polyline
import network.EchoWebSocketListener
import network.WebSocketClient

import org.json.JSONObject

import kotlin.concurrent.thread
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {
    private lateinit var map: GoogleMap
    private val locationPermissionName = Manifest.permission.ACCESS_FINE_LOCATION
    private lateinit var location: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsBinding
    private var currentLocationMarker: Marker? = null
    private var lastLocation: Location? = null

    private var partnerLastLocation: LatLng? = null
    private var destinyMarker : Marker? = null

    private var lastWrittenLocation: Location? = null
    private val locations = mutableListOf<LatLng>()
    private var primerZoom = false

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            }
        })

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private val umbralBajo = 50f
    private lateinit var webSocketClient: WebSocketClient
    private var id: String? = null

    private lateinit var destino: String
    private var destinoConsedido = false
    private var currentPolyline: Polyline? = null
    private var currentPartnerPolyline: Polyline? = null


    private lateinit var mGeocoder: Geocoder

    private lateinit var sensorManagerBrujula: SensorManager
    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    private var rotationMatrix = FloatArray(9)
    private var orientationAngles = FloatArray(3)
    private var cambio: Float = 0.0f
    private var lat: String? = null
    private var long: String? = null
    private var partnerLocationMarker: Marker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManagerBrujula = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        binding.posicionar.setOnClickListener {
            currentLocationMarker?.position?.let { pos -> moveMarkerToLocation(pos) }
        }


        val drawable = resources.getDrawable(R.drawable.flecha_correcta, null)

        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.rojoClaro, null))

        binding.orientacion.setImageDrawable(wrappedDrawable)

        id = intent.getStringExtra("id")
        Log.i("ID desde mapa", id!!)
        webSocketClient = WebSocketClient(
            "ws://ws0nr9l7-8080.use2.devtunnels.ms/api/user/ws/${id!!}",
            EchoWebSocketListener(applicationContext)
        )
        Log.i("ID WEB SOCKET", webSocketClient.toString())

        setupLocation()
        setupMap()
        setupPermissionRequest()
        setupSensor()
        setupGeocoder()
        setupSearchView()

    }

    private fun setupLocation() {
        location = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = createLocationRequest()
        locationCallback = createLocationCallback()
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupPermissionRequest() {
        permissionRequest.launch(locationPermissionName)
    }

    private fun setupSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun setupGeocoder() {
        mGeocoder = Geocoder(this)
    }

    private fun setupSearchView() {
        binding.buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { findAddress(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun moveMarkerToLocation(latLng: LatLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }


    // Sensor luz
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return  // Maneja el caso de que el evento sea null

        when (event.sensor.type) {
            Sensor.TYPE_LIGHT -> {
                if (::map.isInitialized) {
                    val lightValue = event.values[0]
                    if (lightValue < umbralBajo) {
                        map.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                this,
                                R.raw.style_night
                            )
                        )
                        binding.distancia.setTextColor(Color.WHITE)
                    } else {
                        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_day))
                        binding.distancia.setTextColor(Color.BLACK)
                    }
                }
            }

            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(
                    event.values,
                    0,
                    accelerometerReading,
                    0,
                    accelerometerReading.size
                )
            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            }
        }

        updateOrientationAngles()
    }


    // Esto no toca implementarlo, pero tiene que estar si o si
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


    // Agregar marcador LongClick y calcula la distancia entre la ubicación actual y el marcador
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setOnMapLongClickListener { latLng ->
            destinyMarker?.remove()
            destinyMarker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Nuevo marcador")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            locations.add(latLng)
            if (locations.size >= 1) {
                requestRouteFromCurrentLocation(map, locations, Color.GREEN, false)
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

            lastLocation?.let { lastKnownLocation ->
                val distance = calculateDistance(
                    lastKnownLocation,
                    latLng
                ) // Calcular distancia usando la función calculateDistance
                val distanceString = String.format(Locale.getDefault(), "%.2f km", distance)
                Toast.makeText(this, "Distancia al marcador: $distanceString", Toast.LENGTH_SHORT)
                    .show()
            } ?: run {
                Toast.makeText(this, "Ubicación actual no disponible", Toast.LENGTH_SHORT).show()
            }

            if (lastLocation == null) {
                startLocationUpdates()
            }
        }
    }

    // Manejar resultados de ubicación, y escribir cuando se mueve
    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.locations.forEach { location ->

                    updateLocationUI(location)

                    if (lastWrittenLocation == null || lastWrittenLocation!!.distanceTo(location) > 30) {
                        lastWrittenLocation = location

                    }

                    lastLocation = location

                }
                if (lastLocation == null && result.locations.isNotEmpty()) {
                    lastLocation = result.locations.last()
                }
            }
        }
    }


    //Actualizar posicion actual
    private fun updateLocationUI(location: Location) {
        lastLocation = location

        val latLng = LatLng(location.latitude, location.longitude)
        currentLocationMarker?.remove()
        currentLocationMarker = map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Ubicación actual")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        if (!primerZoom) {
            lastWrittenLocation = location
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            primerZoom = true
        }

        val sharedPref = applicationContext.getSharedPreferences("miPref", Context.MODE_PRIVATE)
        lat = sharedPref.getString("latitude", null)
        long = sharedPref.getString("longitude", null)
        if (lat != null && long != null) {
            Log.i("POSICION MAPS", "$lat + $long")

            val posicion = LatLng(lat!!.toDouble(), long!!.toDouble())
            partnerLastLocation = posicion

            partnerLocationMarker?.remove()
            partnerLocationMarker = map.addMarker(
                MarkerOptions()
                    .position(posicion)
                    .title("Partner Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            calcularDistancia()
            orientarUsuario()
            if (destinoConsedido) {
                //updateRoute(posicion)
                try {
                    val addresses = mGeocoder.getFromLocationName(destino, 2)
                    if (!addresses.isNullOrEmpty()) {
                        val addressResult = addresses[0]
                        val position = LatLng(addressResult.latitude, addressResult.longitude)
                        lastLocation?.let { currentLocation ->
                            // Solicitar y trazar la ruta desde la ubicación actual hasta el marcador
                            val waypoints = listOf(currentLocation.toLatLng(), position)
                            requestRouteFromCurrentLocation(map, waypoints, Color.BLUE, false)
                        } ?: Toast.makeText(
                            this,
                            "Ubicación actual no disponible",
                            Toast.LENGTH_SHORT
                        ).show()


                        // Solicitar y trazar la ruta desde la ubicación actual hasta el marcador
                        val partnerWaypoints = listOf(partnerLastLocation!!, position)
                        requestRouteFromCurrentLocation(map, partnerWaypoints, Color.RED, true)

                    } else {
                        Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show()
                    }


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this,
                        "Error procesando la dirección: ${e.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


        } else {
            Log.e(
                "POSICION MAPS",
                "No se encontraron datos de latitud y longitud en SharedPreferences"
            )
        }


    }

    //Iniciar mapa si se concede permiso
    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                locationPermissionName
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            location.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }


    // Solicitar actualizaciones de ubicación
    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        return locationRequest
    }


    // Buscador
    private fun findAddress(addressString: String) {
        destino = addressString
        destinoConsedido = true
        if (addressString.isNotEmpty()) {
            try {
                val addresses = mGeocoder.getFromLocationName(addressString, 2)
                if (!addresses.isNullOrEmpty()) {
                    val addressResult = addresses[0]
                    val position = LatLng(addressResult.latitude, addressResult.longitude)
                    destinyMarker?.remove()
                    destinyMarker = map.addMarker(
                        MarkerOptions().position(position)
                            .title(addressResult.featureName)
                            .snippet(addressResult.getAddressLine(0))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))

                    lastLocation?.let { currentLocation ->
                        val distance = calculateDistance(currentLocation, position)
                        val distanceString = String.format(Locale.getDefault(), "%.2f km", distance)
                        Toast.makeText(
                            this,
                            "Distancia al marcador: $distanceString km",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Solicitar y trazar la ruta desde la ubicación actual hasta el marcador
                        val waypoints = listOf(currentLocation.toLatLng(), position)
                        requestRouteFromCurrentLocation(map, waypoints, Color.BLUE, false)
                    } ?: Toast.makeText(this, "Ubicación actual no disponible", Toast.LENGTH_SHORT)
                        .show()

                    val partnerWaypoints = listOf(partnerLastLocation!!, position)
                    requestRouteFromCurrentLocation(map, partnerWaypoints, Color.RED, true)

                } else {
                    Toast.makeText(this, "Dirección no encontrada", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    this,
                    "Error procesando la dirección: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(this, "La dirección está vacía", Toast.LENGTH_SHORT).show()
        }
    }


    // Función para calcular la distancia entre la ubicación actual y un marcador
    private fun calculateDistance(currentLocation: Location, markerLatLng: LatLng): Float {
        val markerLocation = Location("").apply {
            latitude = markerLatLng.latitude
            longitude = markerLatLng.longitude
        }
        return currentLocation.distanceTo(markerLocation) / 1000 // Convertir a kilómetros
    }

    // Convierte un objeto Location en un objeto LatLng
    private fun Location.toLatLng(): LatLng {
        return LatLng(this.latitude, this.longitude)
    }

    private fun requestRouteFromCurrentLocation(
        map: GoogleMap,
        waypoints: List<LatLng>,
        color: Int,
        partner : Boolean
    ) {
        thread {
            val url = URL(buildRouteUrl(waypoints))
            val connection = url.openConnection() as HttpsURLConnection
            connection.connect()

            val data = connection.inputStream.bufferedReader().readText()
            val jsonData = JSONObject(data)

            val routes = jsonData.optJSONArray("routes")
            if (routes != null && routes.length() > 0) {
                val points =
                    routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")

                val polylineOptions = PolylineOptions()
                decodePoly(points).forEach {
                    polylineOptions.add(it)
                }
                polylineOptions.width(10f).color(color)

                if (partner) {
                    runOnUiThread {
                        // Si currentPolyline es null, significa que es la primera vez que se dibuja la ruta
                        if (currentPolyline == null) {
                            currentPolyline = map.addPolyline(polylineOptions)
                        } else {
                            // Si currentPolyline no es null, actualiza sus puntos con la nueva ruta
                            currentPolyline?.points = polylineOptions.points
                        }
                    }
                } else {
                    runOnUiThread {
                        // Si currentPolyline es null, significa que es la primera vez que se dibuja la ruta
                        if (currentPartnerPolyline == null) {
                            currentPartnerPolyline = map.addPolyline(polylineOptions)
                        } else {
                            // Si currentPolyline no es null, actualiza sus puntos con la nueva ruta
                            currentPartnerPolyline?.points = polylineOptions.points
                        }
                    }
                }

            } else {
                runOnUiThread {
                    Toast.makeText(
                        this@MapsActivity,
                        "No se pudo trazar la ruta",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // Método para dibujar la ruta en el mapa
    private fun drawRouteOnMap(map: GoogleMap, polylineOptions: PolylineOptions, color: Int) {
        map.addPolyline(polylineOptions.color(color))
    }

    // Crea una URL para solicitar direcciones usando la API de Google Maps
    private fun buildRouteUrl(waypoints: List<LatLng>): String {
        //val origin = LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
        val origin = waypoints.first()
        val destination = waypoints.last()
        val waypointsString = waypoints.dropLast(1)
            .joinToString("|") { "via:${it.latitude},${it.longitude}" }

        return "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}&" +
                "destination=${destination.latitude},${destination.longitude}&" +
                "waypoints=$waypointsString&" +
                "key=AIzaSyBbGE3udkS78nvyZ1Ei_nSWLKp3129NRP0" // Clave API
    }


    // Método para decodificar los puntos de la polilínea  (NO SE TOCA)
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else (result shr 1)
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else (result shr 1)
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    //-----------------------------------Brujula---------------------------------------------------//


    private fun updateOrientationAngles() {
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        // Convertir los ángulos de radianes a grados y corregir la inclinación
        val azimuthDegrees = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
        val azimuth = (azimuthDegrees + 360) % 360

        if (Math.abs(azimuth - cambio) > 15) { // Filtrar pequeños cambios para suavizar la rotación
            binding.flecha.rotation = azimuth
            cambio = azimuth
            Log.i("CAMBIO", "Ángulo actualizado a $azimuth grados")
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magnetometer ->
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    //------------------------------Direccion hacia el otro usuario---------------------------------//

    private fun orientarUsuario() {

        val deltaX = lat!!.toDouble() - lastLocation!!.latitude
        val deltaY = long!!.toDouble() - lastLocation!!.longitude

        val anguloRad = Math.atan2(deltaY, deltaX)
        var angulo = Math.toDegrees(anguloRad).toFloat()

        // Ajustar el ángulo para que esté en el rango de 0 a 360 grados
        if (angulo < 0) {
            angulo += 360f
        }

        binding.orientacion.rotation = angulo
    }

    private fun calcularDistancia() {
        val radioTierra = 6371 // Radio de la Tierra en kilómetros
        val dLat = Math.toRadians(lat!!.toDouble() - lastLocation!!.latitude)
        val dLon = Math.toRadians(long!!.toDouble() - lastLocation!!.longitude)

        val a = sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lastLocation!!.latitude)) * cos(
            Math.toRadians(lat!!.toDouble())
        ) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distancia = radioTierra * c

        val texto = String.format("%.${1}f", distancia)

        binding.distancia.text = texto + "Km"
    }

}
