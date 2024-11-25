package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.ComponentActivity

// La actividad principal del menú
class Menu : ComponentActivity() {

    // Variable para controlar la acción de "presionar atrás"
    private var backPressedOnce = false

    // Método que se ejecuta cuando la actividad es creada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity) // Establecemos el layout de la actividad

        // Obtenemos el botón de menú desde el layout
        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configuramos un listener para detectar cuando el usuario presiona el botón
        menuButton.setOnClickListener { view ->
            showPopupMenu(view) // Llamamos al método para mostrar el menú emergente
        }
    }

    // Sobrescribimos el método onBackPressed para personalizar la acción del botón de atrás
    override fun onBackPressed() {
        // Verificamos si el usuario ya ha presionado atrás una vez
        if (backPressedOnce) {
            // Si lo ha hecho, cerramos la actividad
            super.onBackPressed()
            return
        }

        // Si es la primera vez que presiona atrás, mostramos un mensaje
        backPressedOnce = true
        Toast.makeText(this, "Toca de nuevo para salir", Toast.LENGTH_SHORT).show()

        // Después de 2 segundos, reiniciamos el contador para permitir al usuario salir
        // si presiona el botón de atrás nuevamente dentro de este intervalo
        Handler(Looper.getMainLooper()).postDelayed({
            backPressedOnce = false
        }, 2000) // 2000 ms = 2 segundos
    }

    // Método que crea y muestra el PopupMenu
    private fun showPopupMenu(view: View) {
        // Creamos un objeto PopupMenu y lo vinculamos al botón presionado
        val popupMenu = PopupMenu(this, view)
        // Inflamos el menú desde un archivo XML de menú
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)

        // Configuramos un listener para los ítems del menú
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            // Dependiendo del ítem seleccionado, se inicia una nueva actividad
            when (item.itemId) {
                R.id.menu_alta_socio -> startActivity(Intent(this, AltaSocio::class.java)) // Abrimos la actividad AltaSocio
                R.id.menu_alta_departamento -> startActivity(Intent(this, AltaDepartamento::class.java)) // Abrimos la actividad AltaDepartamento
                R.id.menu_baja_socio -> startActivity(Intent(this, BajaSocio::class.java)) // Abrimos la actividad BajaSocio
                R.id.menu_consultar_codigo -> startActivity(Intent(this, ConsultarporCodigoSocio::class.java)) // Abrimos la actividad ConsultarporCodigoSocio
                R.id.menu_consultar_nombre -> startActivity(Intent(this, ConsultarporNombreSocio::class.java)) // Abrimos la actividad ConsultarporNombreSocio
                R.id.menu_consultar_departamento -> startActivity(Intent(this, ConsultaDepartamento::class.java)) // Abrimos la actividad ConsultaDepartamento
                R.id.menu_modificar_socio -> startActivity(Intent(this, ModificacionSocio::class.java)) // Abrimos la actividad ModificacionSocio
                R.id.menu_detalle_socio -> startActivity(Intent(this, DetalleSocios::class.java)) // Abrimos la actividad DetalleSocios
            }
            true // Indicamos que el ítem ha sido procesado
        }
        // Mostramos el menú emergente
        popupMenu.show()
    }
}
