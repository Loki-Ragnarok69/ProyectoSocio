package com.example.proyectosocio

// Importación de las clases necesarias para manejar la base de datos, la interfaz de usuario y la funcionalidad de la actividad
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

// Definición de la clase AltaSocio que extiende de ComponentActivity
class AltaSocio: ComponentActivity() {
    // Método onCreate() que se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout de la actividad
        setContentView(R.layout.alta_socio_activity)

        // Referencia al botón de menú para abrir el PopupMenu
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view) // Llama a la función para mostrar el menú emergente
        }

        // Referencias a los campos de entrada de texto donde el usuario introduce la información del socio
        val nombreSocio: EditText = findViewById(R.id.nombreSocio)
        val apellidosSocio: EditText = findViewById(R.id.apellidosSocio)
        val direccionSocio: EditText = findViewById(R.id.direccionSocio)
        val telefonoSocio: EditText = findViewById(R.id.telefonoSocio)
        val spinnerDepartamentos: Spinner = findViewById(R.id.spinnerDepartamentos)

        // Referencia al botón de "Guardar" para guardar los datos del socio
        val guardarSocios: Button = findViewById(R.id.guardar)

        // Se crea una instancia del objeto AdminSQLiteOpenHelper para interactuar con la base de datos
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)

        // Carga los departamentos en el spinner usando un método personalizado en AdminSQLiteOpenHelper
        admin.loadDepartamentosToSpinner(this, spinnerDepartamentos)

        // Configura un listener para el botón "Guardar"
        guardarSocios.setOnClickListener {
            try {
                // Abre la base de datos en modo escritura
                val bd = admin.writableDatabase
                val registro = ContentValues() // Crea un objeto ContentValues para almacenar los datos

                // Obtener los valores de los campos introducidos por el usuario
                val nombre = nombreSocio.text.toString().trim()
                val apellidos = apellidosSocio.text.toString().trim()
                val direccion = direccionSocio.text.toString().trim()
                val telefono = telefonoSocio.text.toString().trim()
                val departamentoSeleccionado = spinnerDepartamentos.selectedItem.toString()

                // Validación de los campos para asegurar que no estén vacíos
                if (nombre.isEmpty()) {
                    Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detiene la ejecución si el nombre está vacío
                }
                if (apellidos.isEmpty()) {
                    Toast.makeText(this, "Los apellidos no pueden estar vacíos", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detiene la ejecución si los apellidos están vacíos
                }
                if (direccion.isEmpty()) {
                    Toast.makeText(this, "La dirección no puede estar vacía", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detiene la ejecución si la dirección está vacía
                }
                if (telefono.isEmpty()) {
                    Toast.makeText(this, "El teléfono no puede estar vacío", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detiene la ejecución si el teléfono está vacío
                }

                // Verifica que se haya seleccionado un departamento válido en el spinner
                if (departamentoSeleccionado == "Seleccione un departamento") {
                    Toast.makeText(this, "Por favor seleccione un departamento válido", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // Detiene la ejecución si no se seleccionó un departamento válido
                }

                // Verifica si ya existe un socio con el mismo nombre y apellidos
                val query = """
                    SELECT rowid FROM socios 
                    WHERE nombre = ? AND apellidos = ?
                """
                // Realiza una consulta SQL para buscar un socio con los mismos datos
                val cursor = bd.rawQuery(query, arrayOf(nombre, apellidos))

                if (cursor.moveToFirst()) {
                    // Si el cursor tiene resultados, significa que ya existe un socio con ese nombre y apellidos
                    Toast.makeText(this, "Ya existe un socio con ese nombre y apellidos", Toast.LENGTH_LONG).show()
                } else {
                    // Si no existe, se procede a insertar el nuevo socio en la base de datos
                    registro.put("nombre", nombre)
                    registro.put("apellidos", apellidos)
                    registro.put("direccion", direccion)
                    registro.put("telefono", telefono)
                    registro.put("nombre_d", departamentoSeleccionado)

                    // Inserta el nuevo socio en la tabla 'socios' de la base de datos
                    bd.insert("socios", null, registro)
                    Toast.makeText(this, "Socio guardado", Toast.LENGTH_LONG).show()

                    // Limpia los campos después de guardar el nuevo socio
                    nombreSocio.setText("")
                    apellidosSocio.setText("")
                    direccionSocio.setText("")
                    telefonoSocio.setText("")
                    spinnerDepartamentos.setSelection(0) // Resetea el spinner a la opción inicial
                }

                cursor.close() // Cierra el cursor después de usarlo
                bd.close() // Cierra la base de datos después de realizar la operación

            } catch (e: Exception) {
                // Si ocurre un error, se muestra un mensaje de error
                Toast.makeText(this, "Error al guardar el socio: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Función que muestra el menú emergente al presionar el botón de menú
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)  // Crea un objeto PopupMenu
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)  // Infla el menú desde el archivo XML

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
        popupMenu.show()  // Muestra el menú emergente
    }

    // Función que se ejecuta cuando el botón "Atrás" es presionado
    override fun onBackPressed() {
        super.onBackPressed()

        // Redirige al menú principal cuando se presiona el botón de atrás
        val intent = Intent(this, Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()  // Cierra la actividad actual
    }
}
