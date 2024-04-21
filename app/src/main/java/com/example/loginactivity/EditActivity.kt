package com.example.loginactivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.loginactivity.databinding.ActivityEditBinding

import java.io.File

class EditActivity : AppCompatActivity() {
    private lateinit var binding :ActivityEditBinding
    private lateinit var uriCamera: Uri
    private var casillaActiva: ImageButton? = null

    private val PERMISSION_REQUEST_CAMERA = 101

    private val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            loadImage(it, casillaActiva?.tag as Int)
        }
    }

    private val getContentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            loadImage(uriCamera, casillaActiva?.tag as Int)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configurarClickListenersCasillas()
    }

    private fun configurarClickListenersCasillas() {
        val casillas = listOf(
            binding.casilla1.findViewById<ImageButton>(R.id.imageButton1),
            binding.casilla2.findViewById<ImageButton>(R.id.imageButton2),
            binding.casilla3.findViewById<ImageButton>(R.id.imageButton3),
            binding.casilla4.findViewById<ImageButton>(R.id.imageButton4),
            binding.casilla5.findViewById<ImageButton>(R.id.imageButton5),
            binding.casilla6.findViewById<ImageButton>(R.id.imageButton6)
        )

        casillas.forEachIndexed { index, imageButton ->
            imageButton.setOnClickListener {
                casillaActiva = it as ImageButton
                it.tag = index
                showPopupMenu(it, index)
            }
        }
    }

    private fun showPopupMenu(view: View, casillaIndex: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_fotos, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_take_photo -> {
                    abrirCamara(casillaIndex)
                    true
                }
                R.id.action_select_gallery -> {
                    abrirGaleria(casillaIndex)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun abrirCamara(casillaIndex: Int) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
        } else {
            val file = File(getExternalFilesDir(null), "picFromCamera_$casillaIndex.jpg")
            uriCamera = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", file)
            getContentCamera.launch(uriCamera)
        }
    }

    private fun abrirGaleria(casillaIndex: Int) {
        getContentGallery.launch("image/*")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara(casillaActiva?.tag as Int)
        }
    }

    private fun loadImage(uri: Uri, casillaIndex: Int) {

        val constraintLayout = getCasillaConstraintLayout(casillaIndex)

        val inputStream = contentResolver.openInputStream(uri)
        val drawable = Drawable.createFromStream(inputStream, uri.toString())
        constraintLayout.background = drawable

    }

    private fun getCasillaConstraintLayout(casillaIndex: Int): ConstraintLayout {
        return when (casillaIndex) {
            0 -> binding.casilla1
            1 -> binding.casilla2
            2 -> binding.casilla3
            3 -> binding.casilla4
            4 -> binding.casilla5
            5 -> binding.casilla6
            else -> throw IllegalArgumentException("Índice de casilla no válido: $casillaIndex")
        }
    }


}