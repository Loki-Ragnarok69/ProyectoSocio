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

class ConsultarporCodigoSocio : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consulta_codigo_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }


        val spinnerSocios: Spinner = findViewById(R.id.spinnerSocios)

        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        admin.loadSociosToSpinner(this, spinnerSocios)

        val datos: TextView = findViewById(R.id.datos)


        // Establecer el listener del Spinner
        spinnerSocios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                // Obtener el socio seleccionado
                val selectedCodigo = spinnerSocios.selectedItem.toString()

                // Verificar si se ha seleccionado un socio válido
                if (selectedCodigo == "Seleccione un socio") {
                    datos.visibility = TextView.GONE // Hacer invisible el TextView
                    return // Salir si no hay selección válida
                }

                // Consulta a la base de datos para obtener información del socio
                val bd = admin.readableDatabase
                val fila = bd.rawQuery(
                    "SELECT nombre, apellidos, direccion, telefono, nombre_d  FROM socios WHERE rowid = ?",
                    arrayOf(selectedCodigo) // Cambia "codigo" por el nombre de la columna que usas para el código
                )

                // Procesar los resultados de la consulta
                if (fila.moveToFirst()) {
                    val nombre = fila.getString(0)
                    val apellidos = fila.getString(1)
                    val direccion = fila.getString(2)
                    val telefono = fila.getString(3)
                    val departamento = fila.getString(4)

                    // Mostrar los datos en el TextView y hacerlo visible
                    datos.text = "Nombre: $nombre\nApellidos: $apellidos\nDirección: $direccion\nTeléfono: $telefono\nDepartamento: $departamento"
                    datos.visibility = TextView.VISIBLE // Hacer visible el TextView
                } else {
                    Toast.makeText(this@ConsultarporCodigoSocio, "No existe un socio con el código seleccionado", Toast.LENGTH_LONG).show()
                    datos.text = "" // Limpiar el TextView si no se encuentra el socio
                    datos.visibility = TextView.GONE // Hacer invisible el TextView
                }

                fila.close() // Cerrar el cursor
                bd.close() // Cerrar la base de datos
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                datos.visibility = TextView.GONE // Hacer invisible si no hay selección
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
                R.id.menu_modificar_socio -> startActivity(Intent(this, ModificacionSocio::class.java))
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