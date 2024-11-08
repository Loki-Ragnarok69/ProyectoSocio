package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class ConsultaDepartamento : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consulta_departamento_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)
        val spinnerDepartamentos: Spinner = findViewById(R.id.spinnerDepartamentos)
        val datos: TextView = findViewById(R.id.datos)
        datos.movementMethod = ScrollingMovementMethod()

        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)

        // Cargar los nombres de los departamentos en el spinner
        admin.loadDetalleDepartamentosToSpinner(this, spinnerDepartamentos)

        // Listener para manejar la selección de un departamento
        spinnerDepartamentos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                val selectedDepartamento = spinnerDepartamentos.selectedItem.toString()

                // Verificar que no sea la opción por defecto
                if (selectedDepartamento == "Seleccione un departamento") {
                    datos.visibility = TextView.GONE
                    return
                }

                // Obtener los socios del departamento seleccionado
                val sociosList = admin.getSociosPorDepartamento(selectedDepartamento)

                // Mostrar los datos de los socios en el TextView
                if (sociosList.isNotEmpty()) {
                    datos.text = sociosList.joinToString("\n\n") { socio ->
                        "Nombre: ${socio.nombre}\nApellidos: ${socio.apellidos}\nDirección: ${socio.direccion}\nTeléfono: ${socio.telefono}\n" +
                                "Departamento: ${socio.nombre_d}"
                    }
                    datos.setBackgroundResource(R.drawable.textview_borde_redondeado)
                    datos.visibility = TextView.VISIBLE
                } else {
                    datos.text = "No hay socios en este departamento"
                    datos.visibility = TextView.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                datos.visibility = TextView.GONE
            }
        }

        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        // Configura las acciones para cada elemento del menú
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_alta_socio -> startActivity(Intent(this, AltaSocio::class.java))
                R.id.menu_alta_departamento -> startActivity(Intent(this, AltaDepartamento::class.java))
                R.id.menu_baja_socio -> startActivity(Intent(this, BajaSocio::class.java))
                R.id.menu_consultar_codigo -> startActivity(Intent(this, ConsultarporCodigoSocio::class.java))
                R.id.menu_consultar_nombre -> startActivity(Intent(this, ConsultarporNombreSocio::class.java))
                R.id.menu_consultar_departamento -> startActivity(Intent(this, ConsultaDepartamento::class.java))
                R.id.menu_modificar_socio -> startActivity(Intent(this, ModificacionSocio::class.java))
                R.id.menu_detalle_socio -> startActivity(Intent(this, DetalleSocios::class.java))
            }
            true
        }
        popupMenu.show() // Muestra el menú
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}