package com.example.glintup

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.example.glintup.databinding.ActivityCumpleanosBinding
import java.util.Calendar

class CumpleanosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCumpleanosBinding
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCumpleanosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarSeleccionDeFecha()

        // Recuperar la lista de información pasada de la actividad anterior
        val informacionLista: ArrayList<String>? = intent.getStringArrayListExtra("informacionList")
        val nuevaInformacionList = informacionLista ?: ArrayList()

        configurarBotonSiguiente(nuevaInformacionList)
    }

    private fun configurarBotonSiguiente(informacionList: ArrayList<String>) {
        binding.siguiente.setOnClickListener {
            if (selectedDate != null) {
                val intent = Intent(this, SeleccionGeneroActivity::class.java)
                val sharedPreferences = getSharedPreferences("prefs_usuario", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("birthdate", selectedDate)
                    apply()
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, seleccione una fecha de nacimiento.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun configurarSeleccionDeFecha() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            // Formatear la fecha seleccionada y mostrarla en los EditText
            val formattedDay = String.format("%02d", selectedDay)
            val formattedMonth = String.format("%02d", selectedMonth + 1) // Mes está indexado desde 0
            binding.etDay.setText(formattedDay)
            binding.etMonth.setText(formattedMonth)
            binding.etYear.setText(selectedYear.toString())
            // Guardar la fecha seleccionada como una cadena en formato año/mes/día
            selectedDate = "$selectedYear/$formattedMonth/$formattedDay"
        }

        val datePickerOnClickListener = View.OnClickListener {
            DatePickerDialog(this@CumpleanosActivity, dateSetListener, year, month, day).show()
        }

        binding.etDay.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener(datePickerOnClickListener)
        }

        binding.etMonth.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener(datePickerOnClickListener)
        }

        binding.etYear.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener(datePickerOnClickListener)
        }
    }
}
