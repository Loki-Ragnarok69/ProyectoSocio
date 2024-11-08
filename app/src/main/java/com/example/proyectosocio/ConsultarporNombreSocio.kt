package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
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

class ConsultarporNombreSocio: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consulta_nombre_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }


        val spinnerSocios: Spinner = findViewById(R.id.spinnerSocios)

        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        admin.loadSociosToSpinnerNombre(this, spinnerSocios)

        val datos: TextView = findViewById(R.id.datos)

        // Establecer el listener del Spinner
        spinnerSocios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                val selectedNombreCompleto = spinnerSocios.selectedItem.toString()

                // Verificar si se ha seleccionado un socio válido
                if (selectedNombreCompleto == "Seleccione un socio") {
                    datos.visibility = TextView.GONE // Hacer invisible el TextView
                    return
                }

                // Consulta a la base de datos usando el nombre completo
                val bd = admin.readableDatabase
                val query = """ 
                    SELECT nombre, apellidos, direccion, telefono, nombre_d 
                    FROM socios 
                    WHERE (nombre || ' ' || apellidos) = ? 
                    """.trimIndent()

                val fila = bd.rawQuery(query, arrayOf(selectedNombreCompleto))

                // Procesar los resultados
                if (fila.moveToFirst()) {
                    val nombreSocio = fila.getString(0)
                    val apellidos = fila.getString(1)
                    val direccion = fila.getString(2)
                    val telefono = fila.getString(3)
                    val departamento = fila.getString(4)

                    datos.text = "Nombre: $nombreSocio\nApellidos: $apellidos\nDirección: $direccion\nTeléfono: $telefono\nDepartamento: $departamento"
                    datos.visibility = TextView.VISIBLE
                } else {
                    Toast.makeText(this@ConsultarporNombreSocio, "No existe un socio con ese nombre completo", Toast.LENGTH_LONG).show()
                    datos.text = ""
                    datos.visibility = TextView.GONE
                }
                fila.close()
                bd.close()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                datos.visibility = TextView.GONE
            }
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
        // Redirige al menú cuando se presiona el botón de atrás
        val intent = Intent(this, Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Cierra esta actividad
    }
}