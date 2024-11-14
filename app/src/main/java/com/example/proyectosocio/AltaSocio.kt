package com.example.proyectosocio

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity

class AltaSocio: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alta_socio_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

        val nombreSocio: EditText = findViewById(R.id.nombreSocio)
        val apellidosSocio: EditText = findViewById(R.id.apellidosSocio)
        val direccionSocio: EditText = findViewById(R.id.direccionSocio)
        val telefonoSocio: EditText = findViewById(R.id.telefonoSocio)
        val spinnerDepartamentos: Spinner = findViewById(R.id.spinnerDepartamentos)

        val guardarSocios: Button = findViewById(R.id.guardar)

        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        admin.loadDepartamentosToSpinner(this, spinnerDepartamentos)

        guardarSocios.setOnClickListener {
            try {
                val bd = admin.writableDatabase
                val registro = ContentValues()

                // Obtener los valores de los campos
                val nombre = nombreSocio.text.toString().trim()
                val apellidos = apellidosSocio.text.toString().trim()
                val direccion = direccionSocio.text.toString().trim()
                val telefono = telefonoSocio.text.toString().trim()
                val departamentoSeleccionado = spinnerDepartamentos.selectedItem.toString()

                // Validar campos de manera individual
                if (nombre.isEmpty()) {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detener el guardado si hay error
                }
                if (apellidos.isEmpty()) {
                    Toast.makeText(this, "Los apellidos no pueden estar vacíos", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detener el guardado si hay error
                }
                if (direccion.isEmpty()) {
                    Toast.makeText(this, "La dirección no puede estar vacía", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detener el guardado si hay error
                }
                if (telefono.isEmpty()) {
                    Toast.makeText(this, "El teléfono no puede estar vacío", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detener el guardado si hay error
                }

                // Verificar que se haya seleccionado un departamento válido
                if (departamentoSeleccionado == "Seleccione un departamento") {
                    Toast.makeText(this, "Por favor seleccione un departamento válido", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Salir del método y no continuar con el guardado
                }

                // Verificar si ya existe un socio con el mismo nombre y apellidos
                val query = """
                    SELECT rowid FROM socios 
                    WHERE nombre = ? AND apellidos = ?
                """
                val cursor = bd.rawQuery(query, arrayOf(nombre, apellidos))

                if (cursor.moveToFirst()) {
                    // Si el cursor tiene resultados, significa que ya existe un socio con ese nombre y apellidos
                    Toast.makeText(this, "Ya existe un socio con ese nombre y apellidos", Toast.LENGTH_LONG).show()
                } else {
                    // Si no existe, proceder a insertar el nuevo socio
                    registro.put("nombre", nombre)
                    registro.put("apellidos", apellidos)
                    registro.put("direccion", direccion)
                    registro.put("telefono", telefono)
                    registro.put("nombre_d", departamentoSeleccionado)

                    // Insertar el socio en la base de datos
                    bd.insert("socios", null, registro)
                    Toast.makeText(this, "Socio guardado", Toast.LENGTH_LONG).show()

                    // Limpiar los campos después de guardar
                    nombreSocio.setText("")
                    apellidosSocio.setText("")
                    direccionSocio.setText("")
                    telefonoSocio.setText("")
                    spinnerDepartamentos.setSelection(0)
                }

                cursor.close() // Cerrar el cursor
                bd.close() // Cerrar la base de datos

            } catch (e: Exception) {
                Toast.makeText(this, "Error al guardar el socio: ${e.message}", Toast.LENGTH_LONG).show()
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