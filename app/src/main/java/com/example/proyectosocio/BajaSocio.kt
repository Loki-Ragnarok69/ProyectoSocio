package com.example.proyectosocio

// Importación de las clases necesarias para mostrar diálogos, manejar la base de datos, la interfaz de usuario, y otras funcionalidades
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity

// Definición de la clase BajaSocio que extiende de ComponentActivity
class BajaSocio : ComponentActivity() {
    // Método onCreate() que se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout de la actividad
        setContentView(R.layout.baja_socio_activity)

        // Referencia al botón de menú para abrir el PopupMenu
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view) // Llama a la función para mostrar el menú emergente
        }

        // Referencia al spinner donde el usuario seleccionará el socio a eliminar
        val spinnerSocios: Spinner = findViewById(R.id.spinnerSocios)

        // Se crea una instancia del objeto AdminSQLiteOpenHelper para interactuar con la base de datos
        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)

        // Carga los socios en el spinner usando un método personalizado en AdminSQLiteOpenHelper
        admin.loadSociosToSpinner(this, spinnerSocios)

        // Referencia al botón "Dar de baja" para eliminar el socio
        val darDeBaja: Button = findViewById(R.id.darDeBaja)

        // Configura un listener para el botón "Dar de baja"
        darDeBaja.setOnClickListener {
            // Obtener el código del socio seleccionado en el spinner
            val codigo = spinnerSocios.selectedItem?.toString()

            // Si no se ha seleccionado un socio válido, mostrar un mensaje de error
            if (codigo == "Seleccione un socio" || codigo.isNullOrEmpty()) {
                Toast.makeText(this, "Por favor seleccione un socio válido", Toast.LENGTH_LONG)
                    .show()
            } else {
                // Si se ha seleccionado un socio, obtener su nombre y apellidos de la base de datos
                val bd = admin.readableDatabase
                val cursor = bd.rawQuery(
                    "SELECT nombre, apellidos FROM socios WHERE rowid = ?",
                    arrayOf(codigo)
                )

                // Si se encuentra el socio, proceder con la eliminación
                if (cursor.moveToFirst()) {
                    val nombre = cursor.getString(0)
                    val apellidos = cursor.getString(1)
                    cursor.close()
                    bd.close()

                    // Crear el mensaje de confirmación para la eliminación
                    val mensaje =
                        "¿Está seguro de que quiere borrar al usuario \"$nombre $apellidos\"?"

                    // Mostrar el diálogo de confirmación
                    AlertDialog.Builder(this).apply {
                        setTitle("Confirmación de eliminación")
                        setMessage(mensaje)
                        setPositiveButton("Sí") { _, _ ->
                            // Si el usuario confirma, proceder a eliminar el socio
                            val writableDb = admin.writableDatabase
                            val rowsDeleted =
                                writableDb.delete("socios", "rowid = ?", arrayOf(codigo))
                            writableDb.close()

                            // Si la eliminación fue exitosa, mostrar un mensaje de éxito
                            if (rowsDeleted == 1) {
                                Toast.makeText(this@BajaSocio, "Socio borrado", Toast.LENGTH_LONG)
                                    .show()

                                // Recargar el spinner con los socios actualizados
                                admin.loadSociosToSpinner(
                                    this@BajaSocio,
                                    spinnerSocios
                                )
                            } else {
                                // Si no se encuentra un socio con ese código, mostrar un mensaje de error
                                Toast.makeText(
                                    this@BajaSocio,
                                    "No existe un socio con dicho código",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        setNegativeButton("No", null) // La opción predeterminada es "No"
                        setCancelable(false) // Evita que el usuario cierre el diálogo con un clic fuera de él
                        show() // Muestra el diálogo de confirmación
                    }
                } else {
                    // Si no se encuentra el socio en la base de datos, mostrar un mensaje de error
                    cursor.close()
                    bd.close()
                    Toast.makeText(this, "No existe un socio con dicho código", Toast.LENGTH_LONG)
                        .show()
                }
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
