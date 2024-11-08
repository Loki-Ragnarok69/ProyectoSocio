package com.example.proyectosocio

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


class BajaSocio : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.baja_socio_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

        val spinnerSocios: Spinner = findViewById(R.id.spinnerSocios)

        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        admin.loadSociosToSpinner(this, spinnerSocios)

        val darDeBaja: Button = findViewById(R.id.darDeBaja)

        darDeBaja.setOnClickListener {
            val codigo =
                spinnerSocios.selectedItem?.toString() // Obtener el código seleccionado del Spinner

            if (codigo == "Seleccione un socio" || codigo.isNullOrEmpty()) {
                Toast.makeText(this, "Por favor seleccione un socio válido", Toast.LENGTH_LONG)
                    .show()
            } else {
                // Obtener el nombre y apellidos del socio seleccionado
                val bd = admin.readableDatabase
                val cursor = bd.rawQuery(
                    "SELECT nombre, apellidos FROM socios WHERE rowid = ?",
                    arrayOf(codigo)
                )

                if (cursor.moveToFirst()) {
                    val nombre = cursor.getString(0)
                    val apellidos = cursor.getString(1)
                    cursor.close()
                    bd.close()

                    // Crear el mensaje de confirmación
                    val mensaje =
                        "¿Está seguro de que quiere borrar al usuario \"$nombre $apellidos\"?"

                    // Mostrar el diálogo de confirmación
                    AlertDialog.Builder(this).apply {
                        setTitle("Confirmación de eliminación")
                        setMessage(mensaje)
                        setPositiveButton("Sí") { _, _ ->
                            // Eliminar el socio si se confirma
                            val writableDb = admin.writableDatabase
                            val rowsDeleted =
                                writableDb.delete("socios", "rowid = ?", arrayOf(codigo))
                            writableDb.close()

                            if (rowsDeleted == 1) {
                                Toast.makeText(this@BajaSocio, "Socio borrado", Toast.LENGTH_LONG)
                                    .show()
                                admin.loadSociosToSpinner(
                                    this@BajaSocio,
                                    spinnerSocios
                                ) // Recargar el Spinner
                            } else {
                                Toast.makeText(
                                    this@BajaSocio,
                                    "No existe un socio con dicho código",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        setNegativeButton("No", null) // La opción predeterminada es "No"
                        setCancelable(false) // Evita que el usuario cierre el diálogo con un clic fuera de él
                        show() // Muestra el diálogo
                    }
                } else {
                    cursor.close()
                    bd.close()
                    Toast.makeText(this, "No existe un socio con dicho código", Toast.LENGTH_LONG)
                        .show()
                }
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