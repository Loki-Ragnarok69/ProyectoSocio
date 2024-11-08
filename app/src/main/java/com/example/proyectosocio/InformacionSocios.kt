package com.example.proyectosocio

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat

class InformacionSocios : ComponentActivity() {

    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.informacion_socios_activity)

        val menuButton: ImageButton = findViewById(R.id.menuButton)

        // Configura el PopupMenu cuando el botón es presionado
        menuButton.setOnClickListener { view ->
            showPopupMenu(view)
        }

        // Detecta el gesto de deslizamiento
        gestureDetector = GestureDetectorCompat(this, GestureListener())

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

    // Sobrescribimos el método onTouchEvent para detectar el gesto de deslizamiento
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    // Clase interna para manejar el gesto de deslizamiento a la derecha
    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - (e1?.x ?: 0f)

            // Detectar solo el deslizamiento a la derecha
            return if (diffX > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                finish()
                true
            } else {
                false
            }
        }
    }
}
