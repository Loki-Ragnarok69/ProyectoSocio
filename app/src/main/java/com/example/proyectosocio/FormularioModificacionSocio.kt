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

// Clase FormularioModificacionSocio que extiende de ComponentActivity
class FormularioModificacionSocio : ComponentActivity() {

    // Método onCreate que se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecemos el layout de la actividad
        setContentView(R.layout.formulario_modificacion_socio_activity)

        // Inicializamos el botón de menú que abre el PopupMenu
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configuramos el listener para mostrar el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)  // Llamamos al método para mostrar el menú emergente
        }

        // Inicializamos los EditText para capturar los datos del socio
        val nombreSocio: EditText = findViewById(R.id.nombreSocio)
        val apellidosSocio: EditText = findViewById(R.id.apellidosSocio)
        val direccionSocio: EditText = findViewById(R.id.direccionSocio)
        val telefonoSocio: EditText = findViewById(R.id.telefonoSocio)
        val spinnerDepartamentos: Spinner = findViewById(R.id.spinnerDepartamentos)
        val botonGuardar: Button = findViewById(R.id.guardar)

        // Obtenemos el nombre completo del socio desde el Intent recibido
        val nombreCompleto = intent.getStringExtra("nombreCompleto")

        // Si no se recibe el nombre completo, mostramos un error y cerramos la actividad
        if (nombreCompleto == null) {
            Toast.makeText(this, "Error: no se recibió el nombre completo del socio", Toast.LENGTH_SHORT).show()
            finish()  // Terminamos la actividad
            return
        }

        // Cargamos los departamentos en el Spinner
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        admin.loadDepartamentosToSpinner(this, spinnerDepartamentos)

        // Accedemos a la base de datos para obtener los datos del socio
        val bd = admin.readableDatabase
        val fila = bd.rawQuery(
            "SELECT nombre, apellidos, direccion, telefono, nombre_d FROM socios WHERE nombre || ' ' || apellidos LIKE ?",
            arrayOf("%$nombreCompleto%")  // Buscamos el socio por nombre completo
        )

        // Inicializamos el botón "Volver" para regresar a la actividad de modificación de socio
        val volver: Button = findViewById(R.id.volver)
        volver.setOnClickListener {
            val volverMenu = Intent(this, ModificacionSocio::class.java)
            startActivity(volverMenu)  // Regresamos a la actividad de ModificaciónSocio
        }

        // Si encontramos el socio en la base de datos, cargamos sus datos en los EditText
        if (fila.moveToFirst()) {
            nombreSocio.setText(fila.getString(0))  // Cargamos el nombre del socio
            apellidosSocio.setText(fila.getString(1))  // Cargamos los apellidos del socio
            direccionSocio.setText(fila.getString(2))  // Cargamos la dirección del socio
            telefonoSocio.setText(fila.getString(3))  // Cargamos el teléfono del socio

            // Aquí seleccionamos el departamento asociado al socio en el spinner
            val departamento = fila.getString(4)

            // Seleccionamos el departamento en el Spinner basado en el valor obtenido
            for (i in 0 until spinnerDepartamentos.count) {
                if (spinnerDepartamentos.getItemAtPosition(i).toString() == departamento) {
                    spinnerDepartamentos.setSelection(i)  // Seleccionamos el departamento correcto
                    break
                }
            }

        } else {
            // Si no se encuentran datos del socio, mostramos un mensaje de error
            Toast.makeText(this, "No se encontraron datos del socio", Toast.LENGTH_LONG).show()
        }
        fila.close()
        bd.close()

        // Guardamos los cambios cuando el usuario presiona el botón "Guardar"
        botonGuardar.setOnClickListener {
            // Obtenemos los nuevos datos introducidos por el usuario
            val nuevoNombre = nombreSocio.text.toString()
            val nuevoApellido = apellidosSocio.text.toString()
            val nuevaDireccion = direccionSocio.text.toString()
            val nuevoTelefono = telefonoSocio.text.toString()
            val nuevoDepartamento = spinnerDepartamentos.selectedItem.toString()

            // Validamos que los campos no estén vacíos
            if (nuevoNombre.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_LONG).show()
                return@setOnClickListener  // Detenemos la ejecución si el campo está vacío
            }
            if (nuevoApellido.isEmpty()) {
                Toast.makeText(this, "Los apellidos no pueden estar vacíos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (nuevaDireccion.isEmpty()) {
                Toast.makeText(this, "La dirección no puede estar vacía", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (nuevoTelefono.isEmpty()) {
                Toast.makeText(this, "El teléfono no puede estar vacío", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Validamos que se haya seleccionado un departamento válido
            if (nuevoDepartamento == "Seleccione un departamento") {
                Toast.makeText(this, "Por favor, seleccione un departamento válido", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Creamos un mensaje de confirmación para modificar los datos del socio
            var nombreCompleto = intent.getStringExtra("nombreCompleto")
            if (nombreCompleto == null) {
                nombreCompleto = ""
            }

            // Mostramos un diálogo de confirmación
            val mensaje = "¿Está seguro de que quiere modificar los datos del usuario \"$nombreCompleto\"?"

            AlertDialog.Builder(this).apply {
                setTitle("Confirmación de modificación")
                setMessage(mensaje)
                setPositiveButton("Sí") { _, _ ->
                    // Si el usuario confirma, guardamos los nuevos datos
                    val writableDb = admin.writableDatabase
                    val values = ContentValues().apply {
                        put("nombre", nuevoNombre)
                        put("apellidos", nuevoApellido)
                        put("direccion", nuevaDireccion)
                        put("telefono", nuevoTelefono)
                        put("nombre_d", nuevoDepartamento)
                    }

                    // Actualizamos los datos del socio en la base de datos
                    val rowsAffected = writableDb.update(
                        "socios", values,
                        "nombre || ' ' || apellidos LIKE ?", arrayOf("%$nombreCompleto%")
                    )
                    writableDb.close()

                    // Verificamos si la actualización fue exitosa
                    if (rowsAffected > 0) {
                        Toast.makeText(this@FormularioModificacionSocio, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                        finish()  // Terminamos la actividad y regresamos a la anterior
                    } else {
                        Toast.makeText(this@FormularioModificacionSocio, "Error al actualizar los datos", Toast.LENGTH_LONG).show()
                    }
                }
                setNegativeButton("No", null)  // Si el usuario cancela, no hacemos nada
                setCancelable(false)  // Impide que el usuario cierre el diálogo tocando fuera
                show()  // Mostramos el diálogo
            }
        }
    }

    // Método para mostrar el menú emergente (PopupMenu)
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        // Configuramos las acciones para cada ítem del menú
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
        popupMenu.show()  // Muestra el menú emergente
    }
}
