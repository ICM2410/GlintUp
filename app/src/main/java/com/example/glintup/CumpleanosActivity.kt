package com.example.glintup

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import com.example.glintup.databinding.ActivityCumpleanosBinding
import java.util.Calendar

class CumpleanosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCumpleanosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCumpleanosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configurarSeleccionDeFecha()
        configurarBotonSiguiente()
    }

    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, SeleccionGeneroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurarSeleccionDeFecha() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            // Formatear la fecha seleccionada y mostrarla en los EditText
            binding.etDay.setText(String.format("%02d", selectedDay))
            binding.etMonth.setText(String.format("%02d", selectedMonth + 1)) // Mes está indexado desde 0
            binding.etYear.setText(selectedYear.toString())
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
