package com.example.proyectosocio

import android.app.AlertDialog
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
import androidx.compose.material3.AlertDialog

class FormularioModificacionSocio : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.formulario_modificacion_socio_activity)

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
        val botonGuardar: Button = findViewById(R.id.guardar)

        // Obtener el nombre completo del socio
        val nombreCompleto = intent.getStringExtra("nombreCompleto")

        if (nombreCompleto == null) {
            Toast.makeText(this, "Error: no se recibió el nombre completo del socio", Toast.LENGTH_SHORT).show()
            finish() // Terminar la actividad si no se recibe el nombre completo
            return
        }

        // Obtener datos del socio y cargar en los EditText
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        admin.loadDepartamentosToSpinner(this, spinnerDepartamentos)

        val bd = admin.readableDatabase
        val fila = bd.rawQuery(
            "SELECT nombre, apellidos, direccion, telefono, nombre_d FROM socios WHERE nombre || ' ' || apellidos LIKE ?",
            arrayOf("%$nombreCompleto%")
        )

        val volver: Button = findViewById(R.id.volver)
        volver.setOnClickListener{
            val volverMenu = Intent(this, ModificacionSocio::class.java)
            startActivity(volverMenu)
        }

        if (fila.moveToFirst()) {
            nombreSocio.setText(fila.getString(0))
            apellidosSocio.setText(fila.getString(1))
            direccionSocio.setText(fila.getString(2))
            telefonoSocio.setText(fila.getString(3))

            // Aquí seleccionamos el departamento asociado al socio en el spinner
            val departamento = fila.getString(4)

            // Seleccionar el departamento en el Spinner
            for (i in 0 until spinnerDepartamentos.count) {
                if (spinnerDepartamentos.getItemAtPosition(i).toString() == departamento) {
                    spinnerDepartamentos.setSelection(i)
                    break
                }
            }

        } else {
            Toast.makeText(this, "No se encontraron datos del socio", Toast.LENGTH_LONG).show()
        }
        fila.close()
        bd.close()

        // Guardar los cambios al hacer clic en "Guardar Socio"
        botonGuardar.setOnClickListener {
            val nuevoNombre = nombreSocio.text.toString()
            val nuevoApellido = apellidosSocio.text.toString()
            val nuevaDireccion = direccionSocio.text.toString()
            val nuevoTelefono = telefonoSocio.text.toString()
            val nuevoDepartamento = spinnerDepartamentos.selectedItem.toString()

            // Validar campos de manera individual
            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Detener el guardado si hay error
            }
            if (nuevoApellido.isEmpty()) {
                Toast.makeText(this, "Los apellidos no pueden estar vacíos", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Detener el guardado si hay error
            }
            if (nuevaDireccion.isEmpty()) {
                Toast.makeText(this, "La dirección no puede estar vacía", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Detener el guardado si hay error
            }
            if (nuevoTelefono.isEmpty()) {
                Toast.makeText(this, "El teléfono no puede estar vacío", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Detener el guardado si hay error
            }

            // Verificar que se haya seleccionado un departamento válido
            if (nuevoDepartamento == "Seleccione un departamento") {
                Toast.makeText(this, "Por favor, seleccione un departamento válido", Toast.LENGTH_LONG).show()
                return@setOnClickListener // Salir del método y no continuar con el guardado
            }

            var nombreCompleto = intent.getStringExtra("nombreCompleto")
            if (nombreCompleto == null) {
                nombreCompleto = ""
            }

            // Crear mensaje de confirmación
            val mensaje = "¿Está seguro de que quiere modificar los datos del usuario \"$nombreCompleto\"?"

            // Mostrar diálogo de confirmación
            AlertDialog.Builder(this).apply {
                setTitle("Confirmación de modificación")
                setMessage(mensaje)
                setPositiveButton("Sí") { _, _ ->
                    // Si se confirma, guardar los cambios
                    val writableDb = admin.writableDatabase
                    val values = ContentValues().apply {
                        put("nombre", nuevoNombre)
                        put("apellidos", nuevoApellido)
                        put("direccion", nuevaDireccion)
                        put("telefono", nuevoTelefono)
                        put("nombre_d", nuevoDepartamento)
                    }

                    val rowsAffected = writableDb.update(
                        "socios", values,
                        "nombre || ' ' || apellidos LIKE ?", arrayOf("%$nombreCompleto%")
                    )
                    writableDb.close()

                    if (rowsAffected > 0) {
                        Toast.makeText(this@FormularioModificacionSocio, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@FormularioModificacionSocio, "Error al actualizar los datos", Toast.LENGTH_LONG).show()
                    }
                }
                setNegativeButton("No", null) // La opción predeterminada es "No"
                setCancelable(false) // Evita que el usuario cierre el diálogo tocando fuera
                show() // Mostrar el diálogo
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
}
