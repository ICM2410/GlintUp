package com.example.glintup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.glintup.databinding.ActivityFotosRecientesBinding
import models.user.uploadImageResponse
import network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FotosRecientesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFotosRecientesBinding
    private lateinit var uriCamera: Uri
    private var casillaActiva: ImageButton? = null
    private var vacias: Boolean = false

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
        binding = ActivityFotosRecientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()

        configurarBotonSiguiente()
        logInformacionRecibida(nuevaInformacionList)
        configurarClickListenersCasillas()

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, MatchActivity::class.java)
            casillasVacias()
            startActivity(intent)
        }
    }

    private fun logInformacionRecibida(informacionList: ArrayList<String>) {
        Log.d("EstiloDeVidaActivity", "Información actual en la lista:")
        informacionList.forEachIndexed { index, info ->
            Log.d("EstiloDeVidaActivity", "Elemento $index: $info")
        }
    }

    private fun casillasVacias() {
        val casillas = listOf(
            binding.casilla1.background,
            binding.casilla2.background,
            binding.casilla3.background,
            binding.casilla4.background,
            binding.casilla5.background,
            binding.casilla6.background
        )
        vacias = casillas.all { it == null }
        val vacias = casillas.all { it == null }
        if (vacias) {
            val context = this
            val drawableUri = Uri.parse("android.resource://${context.packageName}/${R.drawable.grogu}")
            enviarImagenPorDefecto(drawableUri, this)
        }

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
        val imageButton = getCasillaImageButton(casillaIndex)
        val constraintLayout = getCasillaConstraintLayout(casillaIndex)
        val inputStream = contentResolver.openInputStream(uri)
        val drawable = Drawable.createFromStream(inputStream, uri.toString())
        constraintLayout.background = drawable
        imageButton.visibility = View.INVISIBLE
        Log.i("IMAGEN", "Imagen cargada en casilla: $casillaIndex")

        cargarImagenBase(uri, this)
        //Esto envia la foto a la base pero aun nose lo del id lo deje igual que como en el taller
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

    private fun getCasillaImageButton(casillaIndex: Int): ImageButton {
        return when (casillaIndex) {
            0 -> binding.casilla1.findViewById(R.id.imageButton1)
            1 -> binding.casilla2.findViewById(R.id.imageButton2)
            2 -> binding.casilla3.findViewById(R.id.imageButton3)
            3 -> binding.casilla4.findViewById(R.id.imageButton4)
            4 -> binding.casilla5.findViewById(R.id.imageButton5)
            5 -> binding.casilla6.findViewById(R.id.imageButton6)
            else -> throw IllegalArgumentException("Índice de casilla no válido: $casillaIndex")
        }
    }

    private fun cargarImagenBase(imagen: Uri, contexto: Context) {
        val file = uriToFile(contexto, imagen)

        if (file != null) {
            val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            RetrofitClient.create(applicationContext).uploadImage(body).enqueue(object :
                Callback<uploadImageResponse> {
                override fun onResponse(
                    call: Call<uploadImageResponse>,
                    response: Response<uploadImageResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.i("UPLOAD IMAGE", "Image uploaded successfully")
                    } else {
                        Log.i("UPLOAD IMAGE", "Error uploading the picture, uploading default image")

                    }
                }

                override fun onFailure(call: Call<uploadImageResponse>, t: Throwable) {
                    Toast.makeText(this@FotosRecientesActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.i("IMAGEN", "La imagen no se pudo enviar = NULL")
        }
    }

    private fun enviarImagenPorDefecto(imagen: Uri, contexto: Context) {
        val file = uriToFile(contexto, imagen)

        if (file != null) {
            val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            RetrofitClient.create(applicationContext).uploadImage(body).enqueue(object :
                Callback<uploadImageResponse> {
                override fun onResponse(
                    call: Call<uploadImageResponse>,
                    response: Response<uploadImageResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.i("UPLOAD IMAGE", "Image uploaded successfully")
                    } else {
                        Log.i("UPLOAD IMAGE", "Error uploading the picture, uploading default image")

                    }
                }

                override fun onFailure(call: Call<uploadImageResponse>, t: Throwable) {
                    Toast.makeText(this@FotosRecientesActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.i("IMAGEN", "La imagen no se pudo enviar = NULL")
        }
    }


    private fun uriToFile(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputFile = File(context.cacheDir, "tempFile")

        return try {
            val outputStream = FileOutputStream(outputFile)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            outputFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }


}
