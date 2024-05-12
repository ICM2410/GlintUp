package com.example.glintup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TableRow
import com.example.glintup.databinding.ActivityOrientacionSexualBinding

class OrientacionSexualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrientacionSexualBinding
    private val opcionesSeleccionadas = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrientacionSexualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarOpciones()
        configurarBotonMostrarGenero()
        configurarBotonSiguiente()

    }

    private fun configurarOpciones() {
        val opciones = resources.getStringArray(R.array.orientacionSexualOpciones)
        val tableLayout = binding.tablaOpciones

        for (opcion in opciones) {
            val checkBox = crearCheckBox(opcion)
            val fila = crearFila(checkBox)
            tableLayout.addView(fila)
        }
    }

    private fun crearCheckBox(texto: String): CheckBox {
        val checkBox = CheckBox(this)
        checkBox.text = texto
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (opcionesSeleccionadas.size >= 2) {
                    checkBox.isChecked = false
                } else {
                    opcionesSeleccionadas.add(texto)
                }
            } else {
                opcionesSeleccionadas.remove(texto)
            }
        }
        return checkBox
    }

    private fun crearFila(checkBox: CheckBox): TableRow {
        val fila = TableRow(this)
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 20)
        checkBox.layoutParams = params
        fila.addView(checkBox)
        return fila
    }

    private fun configurarBotonMostrarGenero() {
        binding.mostrarGenero.setOnClickListener {

        }
    }
    private fun configurarBotonSiguiente() {
        binding.siguiente.setOnClickListener {
            val intent = Intent(this, DistanciaActivity::class.java)
            startActivity(intent)
        }
    }
}
