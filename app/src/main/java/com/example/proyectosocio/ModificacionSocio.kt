package com.example.proyectosocio

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

class ModificacionSocio : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificacion_socio_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }


        val spinnerSocios: Spinner = findViewById(R.id.spinnerSocios)

        val admin = AdminSQLiteOpenHelper(this, "administracion.bd", null, 1)
        admin.loadSociosToSpinnerNombre(this, spinnerSocios)

        val botonModificar: Button = findViewById(R.id.botonModificarSocio)
        // Establecer acción del botón Modificar
        botonModificar.setOnClickListener {
            val selectedSocio = spinnerSocios.selectedItem.toString()

            if (selectedSocio == "Seleccione un socio") {
                Toast.makeText(this, "Por favor, seleccione un socio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener el nombre completo del socio (sin separar por espacio)
            val nombreCompleto = selectedSocio

            // Iniciar la actividad de modificación y pasar el nombre completo
            val intent = Intent(this, FormularioModificacionSocio::class.java)
            intent.putExtra("nombreCompleto", nombreCompleto)
            startActivity(intent)
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