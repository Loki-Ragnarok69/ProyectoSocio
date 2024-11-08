package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.ComponentActivity

class InformacionSocios : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.informacion_socios_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

        val socio = intent.getSerializableExtra("socio") as? Socio

        val textNombre: TextView = findViewById(R.id.textNombre)
        val textApellido: TextView = findViewById(R.id.textApellido)
        val textDireccion: TextView = findViewById(R.id.textDireccion)
        val textTelefono: TextView = findViewById(R.id.textTelefono)
        val textDepartamento: TextView = findViewById(R.id.textDepartamento)

        socio?.let {
            textNombre.text = "Nombre: ${it.nombre}"
            textApellido.text = "Apellido: ${it.apellidos}"
            textDireccion.text = "Dirección: ${it.direccion}"
            textTelefono.text = "Teléfono: ${it.telefono}"
            textDepartamento.text = "Departamento: ${it.nombre_d}"
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
}
