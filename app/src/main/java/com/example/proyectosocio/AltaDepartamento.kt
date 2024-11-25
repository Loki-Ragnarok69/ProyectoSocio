package com.example.proyectosocio

// Importación de las bibliotecas necesarias para la actividad y otros elementos
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity

// Definición de la clase AltaDepartamento que extiende de ComponentActivity
class AltaDepartamento : ComponentActivity() {
    // Método onCreate() que se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alta_departamento_activity)  // Establece el layout de la actividad

        // Referencia al botón de menú para abrir el PopupMenu
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)  // Llama a la función para mostrar el menú emergente
        }

        // Referencias a los campos de entrada de texto para capturar los datos del departamento
        val nombreDepartamento: EditText = findViewById(R.id.nombreDepartamento)
        val plantaDepartamento: EditText = findViewById(R.id.plantaDepartamento)
        val direccionDepartamento: EditText = findViewById(R.id.direccionDepartamento)

        // Referencia al botón "Guardar" para guardar los datos introducidos
        val guardarDepartamento: Button = findViewById(R.id.guardar)

        // Configura un listener para el botón "Guardar"
        guardarDepartamento.setOnClickListener {
            // Obtener los valores ingresados en los campos de texto y eliminar espacios al principio y al final
            val nombre = nombreDepartamento.text.toString().trim()
            val planta = plantaDepartamento.text.toString().trim()
            val direccion = direccionDepartamento.text.toString().trim()

            // Validación de los campos para asegurarse de que no estén vacíos
            if (nombre.isEmpty()) {
                // Si el nombre está vacío, muestra un mensaje y detiene la ejecución
                Toast.makeText(this, "El nombre del departamento no puede estar vacío", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (planta.isEmpty()) {
                // Si la planta está vacía, muestra un mensaje y detiene la ejecución
                Toast.makeText(this, "La planta del departamento no puede estar vacía", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (direccion.isEmpty()) {
                // Si la dirección está vacía, muestra un mensaje y detiene la ejecución
                Toast.makeText(this, "La dirección del departamento no puede estar vacía", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Si todos los campos son válidos, se continúa con el proceso de guardado
            try {
                // Crea una instancia de la clase AdminSQLiteOpenHelper para interactuar con la base de datos
                val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
                val bd = admin.writableDatabase  // Abre la base de datos en modo escritura
                val registro = ContentValues()  // Crea un objeto ContentValues para almacenar los datos

                // Rellenar los valores del registro con los datos del departamento
                registro.put("nombre", nombre)
                registro.put("planta", planta)
                registro.put("direccion", direccion)

                // Inserta el registro en la tabla 'departamentos' de la base de datos
                bd.insert("departamentos", null, registro)
                bd.close()  // Cierra la base de datos

                // Limpia los campos de entrada después de guardar el departamento
                nombreDepartamento.setText("")
                plantaDepartamento.setText("")
                direccionDepartamento.setText("")

                // Muestra un mensaje de éxito
                Toast.makeText(this, "Departamento guardado correctamente", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Si ocurre algún error al guardar, muestra un mensaje de error
                Toast.makeText(this, "Error al guardar el departamento: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Función para mostrar el menú emergente cuando se presiona el botón de menú
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)  // Crea el objeto PopupMenu
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)  // Infla el menú desde el archivo XML

        // Configura las acciones para cada elemento del menú
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                // Cuando se selecciona un ítem del menú, se inicia la actividad correspondiente
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

    // Función que se ejecuta cuando el botón de "atrás" es presionado
    override fun onBackPressed() {
        super.onBackPressed()

        // Redirige al menú principal cuando se presiona el botón de atrás
        val intent = Intent(this, Menu::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()  // Cierra la actividad actual
    }
}
