package com.example.proyectosocio

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat


class Menu : ComponentActivity() {
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

//        val botonAltaSocio: Button = findViewById(R.id.botonAltaSocio)
//        val botonAltaDepartamento: Button = findViewById(R.id.botonAltaDepartamento)
//        val botonBajaSocio: Button = findViewById(R.id.botonBajaSocio)
//        val botonConsultarCodigo: Button = findViewById(R.id.botonConsultarCodigo)
//        val botonConsultarNombre: Button = findViewById(R.id.botonConsultarNombre)
//        val botonModificarSocio: Button = findViewById(R.id.botonModificarSocio)
//
//        botonAltaSocio.setOnClickListener{
//            val actividadAltaSocio= Intent(this, AltaSocio::class.java)
//            startActivity(actividadAltaSocio)
//        }
//
//        botonAltaDepartamento.setOnClickListener{
//            val actividadAltaDepartamento = Intent(this, AltaDepartamento::class.java)
//            startActivity(actividadAltaDepartamento)
//        }
//
//        botonConsultarCodigo.setOnClickListener{
//            val actividadConsultarporCodigoSocio = Intent(this, ConsultarporCodigoSocio::class.java)
//            startActivity(actividadConsultarporCodigoSocio)
//        }
//
//        botonConsultarNombre.setOnClickListener{
//            val actividadConsultarporCodigoSocio = Intent(this, ConsultarporNombreSocio::class.java)
//            startActivity(actividadConsultarporCodigoSocio)
//        }
//
//        botonBajaSocio.setOnClickListener{
//            val actividadBajaSocio = Intent(this, BajaSocio::class.java)
//            startActivity(actividadBajaSocio)
//        }
//
//        botonModificarSocio.setOnClickListener{
//            val actividadModificacionSocio = Intent(this, ModificacionSocio::class.java)
//            startActivity(actividadModificacionSocio)
//        }

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

    }

    override fun onBackPressed() {
        if (backPressedOnce) {
            // Si ya presionó el botón de atrás una vez, cerramos la actividad
            super.onBackPressed() // Esto cierra la actividad
            return
        }

        // Si es la primera vez que presiona el botón de atrás, mostramos el mensaje
        backPressedOnce = true
        Toast.makeText(this, "Toca de nuevo para salir", Toast.LENGTH_SHORT).show()

        // Después de 2 segundos (el tiempo entre los dos toques), reiniciamos el contador
        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000) // 2000 ms = 2 segundos
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
