package com.example.glintup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.glintup.databinding.ActivityUserBinding
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding

    private lateinit var uriCamera: Uri

    private val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        // Lógica para manejar la imagen seleccionada de la galería
        loadImage(uri)
    }

    private val getContentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Lógica para manejar la imagen capturada por la cámara
            loadImage(uriCamera)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navegacion.menu.getItem(4).isChecked = true

        configurarBotonSiguiente()
        Configuration()

        val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)

        val nombre = sharedPreferences.getString("nombre", null)
        val cumple = sharedPreferences.getString("birthdate", null)
        val foto = sharedPreferences.getString("foto", null)
        val id = sharedPreferences.getString("id", null)

        Log.i("INFO USUARIO", "$nombre $cumple $foto $id")
        if (cumple != null) {
            val age = calculateAge(cumple)

            binding.nombreEdad.text = "$nombre, $age"+ " años"
        } else {
            binding.nombreEdad.text = "$nombre"
        }
        if (foto != null) {
            val uri = Uri.parse(foto)
            loadImage(uri)
        }

        binding.navegacion.setOnItemSelectedListener {
            navigateToItem(it.itemId)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.navegacion.menu.getItem(4).isChecked = true

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

    fun calculateAge(birthdate: String): Int {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val birthDate = LocalDate.parse(birthdate, formatter)
        val currentDate = LocalDate.now()
        return Period.between(birthDate, currentDate).years
    }


    private fun Configuration() {
        binding.ajustes.setOnClickListener {
            showPopupMenuConfi(it)
        }
    }

    private fun showPopupMenuConfi(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.editar_perfil, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.editarPerfil -> {

                    val intent = Intent(this, EditActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.close -> {
                    val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    startActivity(Intent(this,SignUpActivity::class.java))

                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun configurarBotonSiguiente() {
        binding.edit.setOnClickListener {
            showPopupMenuPhotos(it)
        }
    }

    private fun showPopupMenuPhotos(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_fotos, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_take_photo -> {

                    abrirCamara()
                    true
                }
                R.id.action_select_gallery -> {

                    abrirGaleria()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun abrirCamara() {
        // Solicitar permisos de cámara si aún no están concedidos
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CAMERA
            )
        } else {
            // Los permisos de cámara ya están concedidos, abrir la cámara
            val file = File(filesDir, "picFromCamera")
            uriCamera = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )
            getContentCamera.launch(uriCamera)
        }
    }

    private fun abrirGaleria() {
        getContentGallery.launch("image/*")
    }

    private fun loadImage(uri: Uri?) {
        // Lógica para cargar la imagen en la vista
        uri?.let {
            binding.imagenPerfil.setImageURI(uri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abrir la cámara
                abrirCamara()
            } else {
                // Permiso denegado, mostrar mensaje de error o tomar otra acción
            }
        }
    }

    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1001
    }


}


