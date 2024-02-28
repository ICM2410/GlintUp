package com.example.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TableRow
import com.example.loginactivity.databinding.ActivityOrientacionSexualBinding

class OrientacionSexualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrientacionSexualBinding
    private val selectedOptions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrientacionSexualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val opciones = resources.getStringArray(R.array.orientacionSexualOpciones)
        val tableLayout = binding.tablaOpciones

        for (opcion in opciones) {
            val checkBox = CheckBox(this)
            checkBox.text = opcion
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (selectedOptions.size >= 3) {
                        checkBox.isChecked = false
                    } else {
                        selectedOptions.add(opcion)
                    }
                } else {
                    selectedOptions.remove(opcion)
                }
            }

            val row = TableRow(this)
            val params = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 20)
            checkBox.layoutParams = params
            row.addView(checkBox)
            tableLayout.addView(row)
        }

        binding.mostrarGenero.setOnClickListener {

        }
    }

}
